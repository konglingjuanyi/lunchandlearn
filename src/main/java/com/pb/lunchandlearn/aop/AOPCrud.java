package com.pb.lunchandlearn.aop;

import com.pb.lunchandlearn.config.ModalCollectionSettings;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import com.pb.lunchandlearn.service.IDProviderService;
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
import org.springframework.util.StringUtils;

import static com.pb.lunchandlearn.utils.CommonUtil.SORT_BY_DEFAULT;
import static com.pb.lunchandlearn.utils.CommonUtil.SORT_BY_SCORE;

/**
 * Created by de007ra on 5/4/2016.
 */
@Component
@Aspect
public class AOPCrud {
	Logger logger = LoggerFactory.getLogger(AOPCrud.class);
	@Autowired
	IDProviderService idProviderService;

	@Autowired
	ModalCollectionSettings modalCollectionSettings;

	@Before("execution(* com.pb.lunchandlearn.service.*Service.add*(..))")
	public void setId(JoinPoint joinPoint) {
		logger.debug("Before Advicing from AOPCrud.setId");
		Object obj = joinPoint.getArgs()[0];
		if (obj instanceof Topic) {
			((Topic) obj).setId(
					idProviderService.getNextId(modalCollectionSettings.getTopic()));
		} else if (obj instanceof Training) {
			Training training = ((Training) obj);
			training.setId(
					idProviderService.getNextId(modalCollectionSettings.getTraining()));
			training.setStatus(TrainingStatus.NOMINATED);
		} else if (obj instanceof Comment) {
			((Comment) obj).setId(
					idProviderService.getNextId(modalCollectionSettings.getComment()));
		}
	}

	@Pointcut("execution(* com.pb.lunchandlearn.repository.*.find*(..))")
	private void findMethods() {}

	@Pointcut("execution(* com.pb.lunchandlearn.repository.*.get*(..))")
	private void getMethods() {}

	@Pointcut("execution(* com.pb.lunchandlearn.service.*.search(String,org.springframework.data.domain.Pageable,..))")
	private void searchMethods() {}

	@Pointcut("execution(* com.pb.lunchandlearn.service.*.getAll(org.springframework.data.domain.Pageable,..))")
	private void getAllMethods() {}

	/*
	@Around("execution(* com.pb.lunchandlearn.service.*.search(..,org.springframework.data.domain.Pageable)) " +
			"&& args(..,pageable)")
*/
	@Around("searchMethods() || getAllMethods()")
	public Object setDefaultSortParam(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		Sort sort = args[0] instanceof String ? StringUtils.isEmpty(args[0]) ? SORT_BY_DEFAULT : SORT_BY_SCORE : SORT_BY_DEFAULT;
		for (int count = 0; count < args.length;++count) {
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
		if(returnObj == null) {
			Class cls = ((MethodSignature)joinPoint.getSignature()).getReturnType();
			if(cls == Training.class) {
				throw new ResourceNotFoundException("Training does not exist");
			}
			else if(cls == Topic.class) {
				throw new ResourceNotFoundException("Topic does not exist");
			}
			if(cls == Employee.class) {
				throw new ResourceNotFoundException("Employee does not exist");
			}
		}
	}
}