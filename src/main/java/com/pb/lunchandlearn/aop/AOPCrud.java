package com.pb.lunchandlearn.aop;

import com.pb.lunchandlearn.config.ModalCollectionSettings;
import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.config.ServiceAccountSettings;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.exception.DuplicateResourceException;
import com.pb.lunchandlearn.exception.InvalidOperationException;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import com.pb.lunchandlearn.repository.FeedbackRepository;
import com.pb.lunchandlearn.repository.TrainingRepository;
import com.pb.lunchandlearn.service.*;
import com.pb.lunchandlearn.service.mail.MailService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static com.pb.lunchandlearn.config.SecurityConfig.getLoggedInUser;
import static com.pb.lunchandlearn.utils.CommonUtil.SORT_BY_DEFAULT;
import static com.pb.lunchandlearn.utils.CommonUtil.SORT_BY_SCORE;

/**
 * Created by de007ra on 5/4/2016.
 */
@Component
@Aspect
public class AOPCrud {
	private Logger logger = LoggerFactory.getLogger(AOPCrud.class);
	@Autowired
	private IDProviderService idProviderService;

	@Autowired
	private ModalCollectionSettings modalCollectionSettings;

	@Autowired
	private TrainingRepository trainingRepository;

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private ServiceAccountSettings serviceAccountSettings;

	@Around("execution(* com.pb.lunchandlearn.service.*Service.add(..))")
	public Object insert(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.debug("Around from AOPCrud.insert()");
		setId(joinPoint.getArgs()[0]);
		Object retVal = joinPoint.proceed();
		sendInsertMail(retVal, joinPoint.getArgs());
		return retVal;
	}

	@AfterReturning(value = "execution(* com.pb.lunchandlearn.service.*Service.update(..))", returning = "retVal")
	public Object update(Object retVal) throws Throwable {
		sendUpdateMail(retVal, null, null);
		return retVal;
	}

	@Around(value = "execution(* com.pb.lunchandlearn.service.TrainingServiceImpl.removeAttachedFile(..))")
	public Object removeTrainingAttachments(ProceedingJoinPoint joinPoint) throws Throwable {
		Long trainingId = (Long) getFirstArgOfType(joinPoint.getArgs(), Long.class);
		Object retVal = joinPoint.proceed();
		if(Boolean.TRUE.equals(retVal)) {
			FileAttachmentInfo fileInfo = new FileAttachmentInfo();
			fileInfo.setFileName((String) getFirstArgOfType(joinPoint.getArgs(), String.class));
			mailService.sendMail(MailService.MailType.ATTACHMENT_REMOVED, fileInfo, trainingId);
		}
		return retVal;
	}

	@Around(value = "execution(* com.pb.lunchandlearn.service.TrainingServiceImpl.removeComment*(..))")
	public Object removeComment(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		Long trainingId = (Long) getFirstArgOfType(args, Long.class);
		Comment cmt = null;
		if(args.length >= 3) {
			cmt = trainingRepository.getCommentReply(trainingId, (Long) args[1], (Long) args[2]);
		}
		else if(args.length >= 2) {
			cmt = trainingRepository.getComment(trainingId, (Long) args[1]);
		}
		Object retVal = joinPoint.proceed();
		if(Boolean.TRUE.equals(retVal)) {
			mailService.sendMail(MailService.MailType.COMMENT_REMOVED, cmt, trainingId);
		}
		return  retVal;
	}

	@Before("execution(* com.pb.lunchandlearn.repository.EmployeeRepository.delete(..))")
	private void deleteEmployee(JoinPoint joinPoint) {
		String guid = (String)joinPoint.getArgs()[0];
		if(StringUtils.equalsIgnoreCase(serviceAccountSettings.getGuid(), guid)) {
			throw new InvalidOperationException(MessageFormat.format("User with Guid '{0}' " +
					"can't be deleted", guid));
		}
	}

	@Around(value = "execution(* com.pb.lunchandlearn.service.*Service.updateField(..))")
	public Object updateField(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.debug("Around from AOPCrud.updateField()");
		Object obj = joinPoint.getSignature().getDeclaringType();
		Collection<String> receipientsGuid = null;
		if(obj instanceof TrainingServiceImpl || joinPoint.getTarget() instanceof TrainingServiceImpl) {
			SimpleFieldEntry entry = (SimpleFieldEntry) getFirstArgOfType(joinPoint.getArgs(), SimpleFieldEntry.class);
			if(TrainingService.isTrainerField(entry.getName())) {
				Long trainingId = (Long) getFirstArgOfType(joinPoint.getArgs(), Long.class);
				Training trn = trainingRepository.getTrainersById(trainingId);
				receipientsGuid = trn.getTrainers().values();
			}
		}
		Object retVal = joinPoint.proceed();
		if(Boolean.TRUE.equals(retVal)) {
			sendUpdateMail(obj, joinPoint, receipientsGuid);
		}
		return retVal;
	}

	private void sendInsertMail(Object obj, Object[] args) {
		if (obj != null) {
			if (obj instanceof Topic) {
				mailService.sendMail(MailService.MailType.TOPIC_ADDED, (Topic) obj);
			} else if (obj instanceof Training) {
				mailService.sendMail(MailService.MailType.TRAINING_ADDED, (Training) obj);
			} else if (obj instanceof Comment) {
				Long trainingId = (Long) getFirstArgOfType(args, Long.class);
				mailService.sendMail(MailService.MailType.COMMENT_ADDED, (Comment) obj, trainingId);
			} else if (obj instanceof FeedBack) {
				mailService.sendMail(MailService.MailType.FEEDBACK_ADDED, (FeedBack) obj);
			} else if (obj instanceof Employee) {
				mailService.sendMail(MailService.MailType.EMPLOYEE_ADDED, (Employee) obj);
			} else if (obj instanceof FileAttachmentInfo) {
				Long trainingId = (Long) getFirstArgOfType(args, Long.class);
				mailService.sendMail(MailService.MailType.ATTACHMENT_ADDED, (FileAttachmentInfo) obj, trainingId);
			}
		}
	}

	private Object getFirstArgOfType(Object[] args, Class c) {
		for (Object arg : args) {
			if (arg.getClass() == c) {
				return arg;
			}
		}
		return null;
	}

	private void sendUpdateMail(Object obj, JoinPoint joinPoint, Collection<String> recipientsGuid) {
		if (obj != null) {
			if (obj instanceof Topic) {
				mailService.sendMail(MailService.MailType.TOPIC_UPDATED, (Topic) obj);
			} else if (obj instanceof Training) {
				mailService.sendMail(MailService.MailType.TRAINING_UPDATED, (Training) obj);
			} else if (obj instanceof Employee) {
				mailService.sendMail(MailService.MailType.EMPLOYEE_UPDATED, (Employee) obj);
			} else if(obj instanceof TopicServiceImpl || joinPoint.getTarget() instanceof TopicServiceImpl) {
				mailService.sendMail(MailService.MailType.TOPIC_UPDATED, (Long) getFirstArgOfType(joinPoint.getArgs(), Long.class));
			} else if(obj instanceof EmployeeServiceImpl || joinPoint.getTarget() instanceof EmployeeServiceImpl) {
				mailService.sendMail(MailService.MailType.EMPLOYEE_UPDATED, (String) getFirstArgOfType(joinPoint.getArgs(), String.class));
			} else if(obj instanceof TrainingServiceImpl || joinPoint.getTarget() instanceof TrainingServiceImpl) {
				SimpleFieldEntry simpleFieldEntry = (SimpleFieldEntry) getFirstArgOfType(joinPoint.getArgs(), SimpleFieldEntry.class);
				Long trainingId = (Long) getFirstArgOfType(joinPoint.getArgs(), Long.class);
				if("status".equalsIgnoreCase(simpleFieldEntry.getName())) {
					TrainingStatus status = TrainingStatus.valueOf(simpleFieldEntry.getValue().toString());
					if(TrainingStatus.SCHEDULED == status) {
						mailService.sendMail(MailService.MailType.TRAINING_SCHEDULED, trainingId, simpleFieldEntry);
					}
					else if(TrainingStatus.CANCELLED == status || status == TrainingStatus.POSTPONED) {
						mailService.sendMail(MailService.MailType.TRAINING_CANCELLED, trainingId);
					}
				}
				else {
					mailService.sendMail(MailService.MailType.TRAINING_UPDATED, trainingId, simpleFieldEntry, recipientsGuid);
				}
			}
		}
	}

	private void setId(Object obj) {
		if (obj instanceof Topic) {
			Topic topic = (Topic) obj;
			topic.setId(
					idProviderService.getNextId(modalCollectionSettings.getTopic()));
			setTopicCreateUser(topic, getLoggedInUser());
			topic.setTrainings(Collections.emptyList());

		} else if (obj instanceof Training) {
			Training training = ((Training) obj);
			training.setId(
					idProviderService.getNextId(modalCollectionSettings.getTraining()));
			training.setStatus(TrainingStatus.NOMINATED);
			training.setCreateDateTime(new Date());
			training.setLikesCount(0);
			training.setComments(Collections.EMPTY_LIST);
			setTrainingCreateUser(training, getLoggedInUser());
		} else if (obj instanceof Comment) {
			Comment cmt = (Comment) obj;
			cmt.setId(
					idProviderService.getNextId(modalCollectionSettings.getComment()));
			setCommentUser(cmt, getLoggedInUser());
		} else if (obj instanceof FeedBack) {
			FeedBack feedBack = (FeedBack) obj;
			if (feedbackRepository.countByParentIdAndRespondentGuid(feedBack.getParentId(),
					feedBack.getRespondentGuid()) > 0) {
				throw new DuplicateResourceException("Feedback already exist");
			}
			feedBack.setId(
					idProviderService.getNextId(modalCollectionSettings.getFeedback()));
			setFeedBackUser(feedBack, getLoggedInUser());
		} else if (obj instanceof Employee) {
			Employee emp = (Employee) obj;
			emp.setGuid(emp.getGuid().toUpperCase());
		}
	}

	//	@Before("updateMethods() || attachmentMethod()")
	public void validateOperation(JoinPoint joinPoint) {
		logger.debug("Before Advicing from AOPCrud.setId");
		Object obj = joinPoint.getArgs()[0];
		if (obj instanceof Topic) {
			Topic topic = (Topic) obj;
			topic.setId(
					idProviderService.getNextId(modalCollectionSettings.getTopic()));
			setTopicModifiedByUser(topic, getLoggedInUser());

		} else if (obj instanceof Training) {
			Training training = ((Training) obj);
			training.setId(
					idProviderService.getNextId(modalCollectionSettings.getTraining()));
			setTrainingModifiedByUser(training, getLoggedInUser());
		}
	}

	/*
	@Around("execution(* com.pb.lunchandlearn.service.*.search(..,org.springframework.data.domain.Pageable)) " +
			"&& args(..,pageable)")
*/
	@Around("searchMethods() || getAllMethods()")
	public Object setDefaultSortParam(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		Sort sort = args[0] instanceof String ? StringUtils.isEmpty((String)args[0]) ? SORT_BY_DEFAULT : SORT_BY_SCORE : SORT_BY_DEFAULT;
		for (int count = 0; count < args.length; ++count) {
			Object obj = args[count];
			if (obj instanceof Pageable) {
				Pageable pageable = (Pageable) obj;
				if (pageable.getSort() == null) {
					args[count] = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
				}
				break;
			}
		}
		return joinPoint.proceed(args);
	}

	@AfterReturning(value = "getMethods() || findMethods()", returning = "returnObj")
	public void checkResource(JoinPoint joinPoint, Object returnObj) {
		logger.debug("After Returning advice");
		if (returnObj == null) {
			Class cls = ((MethodSignature) joinPoint.getSignature()).getReturnType();
			if (cls == Training.class) {
				throw new ResourceNotFoundException("Training does not exist");
			} else if (cls == Topic.class) {
				throw new ResourceNotFoundException("Topic does not exist");
			}
			if (cls == Employee.class) {
				throw new ResourceNotFoundException("Employee does not exist");
			}
		}
	}

	@Pointcut("execution(* com.pb.lunchandlearn.repository.*.find*(..))")
	private void findMethods() {
	}

	@Pointcut("execution(* com.pb.lunchandlearn.repository.*.get*(..))")
	private void getMethods() {
	}

	@Pointcut("execution(* com.pb.lunchandlearn.service.*.search(String,org.springframework.data.domain.Pageable,..))")
	private void searchMethods() {
	}

	@Pointcut("execution(* com.pb.lunchandlearn.service.*.getAll(org.springframework.data.domain.Pageable,..))")
	private void getAllMethods() {
	}

	@Pointcut("execution(* com.pb.lunchandlearn.repository.*Repository*.update*(..))")
	private void updateMethods() {
	}

	@Pointcut("execution(* com.pb.lunchandlearn.service.*Service.attachFile(..))")
	private void attachmentMethod() {
	}

	private void setFeedBackUser(FeedBack feedBack, SecuredUser user) {
		feedBack.setRespondentGuid(user.getGuid());
		feedBack.setRespondentName(user.getUsername());
	}

	private void setTopicCreateUser(Topic topic, SecuredUser user) {
		topic.setCreatedByGuid(user.getGuid());
		topic.setCreatedByName(user.getUsername());
	}

	private void setTopicModifiedByUser(Topic topic, SecuredUser user) {
		topic.setLastModifiedByGuid(user.getGuid());
		topic.setLastModifiedByName(user.getUsername());
	}

	private void setTrainingCreateUser(Training training, SecuredUser user) {
		training.setCreatedByGuid(user.getGuid());
		training.setCreatedByName(user.getUsername());
	}

	private void setTrainingModifiedByUser(Training topic, SecuredUser user) {
		topic.setLastModifiedByGuid(user.getGuid());
		topic.setLastModifiedByName(user.getUsername());
	}

	private void setCommentUser(Comment comment, SecuredUser user) {
		comment.setOwnerGuid(user.getGuid());
		comment.setOwnerName(user.getUsername());
	}
}