package com.pb.lunchandlearn.service.mail;

import com.pb.lunchandlearn.config.ApplicationConfiguration;
import com.pb.lunchandlearn.config.MailServerSettings;
import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.config.ServiceAccountSettings;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.repository.EmployeeRepository;
import com.pb.lunchandlearn.repository.TopicRepository;
import com.pb.lunchandlearn.repository.TrainingRepository;
import com.pb.lunchandlearn.service.LDAPService;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.pb.lunchandlearn.config.SecurityConfig.getLoggedInUser;
import static com.pb.lunchandlearn.service.EmployeeServiceImpl.ADMIN_ROLE_LIST;

public final class MailingTask implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(MailingTask.class);
	private static DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

	private String[] to;
	private String subject;
	private String msg;
	private MailService.MailType mailType;
	private Training training;
	private SecuredUser securedUser = getLoggedInUser();
	private Comment comment = null;
	private Employee employee = null;
	private FileAttachmentInfo fileAttachmentInfo = null;
	private Topic topic;
	private boolean isCalenderRequest;

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private TrainingRepository trainingRepository;
	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private ApplicationConfiguration applicationConfiguration;
	@Autowired
	private MailServerSettings mailServerSettings;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private LDAPService ldapService;

	private MimeMessage mimeMessage;

	@Autowired
	private ServiceAccountSettings serviceAccountSettings;
	private String from;

	private Long parentId;
	private String employeeGuid;
	private String updatedFieldName;
	private Object updatedFieldValue;
	private Collection<String> recipientsGuid;

	public void setUpdatedFieldValue(Object updatedFieldValue) {
		this.updatedFieldValue = updatedFieldValue;
	}

	public void setMailType(MailService.MailType mailType) {
		this.mailType = mailType;
	}

	public Training getTraining() {
		return training;
	}

	public void setTraining(Training training) {
		this.training = training;
	}

	public void setUpdatedFieldName(String updatedFieldName) {
		this.updatedFieldName = updatedFieldName;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void setFileAttachmentInfo(FileAttachmentInfo fileAttachmentInfo) {
		this.fileAttachmentInfo = fileAttachmentInfo;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public void setEmployeeGuid(String employeeGuid) {
		this.employeeGuid = employeeGuid;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public MailingTask() {
	}

	private void sendMail() {
		isCalenderRequest = false;
		Set<String> mailingSet;
		mailingSet = getEmailsByRoles(ADMIN_ROLE_LIST);
		if(!CollectionUtils.isEmpty(recipientsGuid)) {
			for(Employee emp : employeeRepository.findAllByGuidIn(recipientsGuid)) {
				mailingSet.add(emp.getEmailId());
			}
		}
		switch (mailType) {
			case EMPLOYEE_ADDED:
				mailingSet.add(employee.getEmailId());
				break;
			case EMPLOYEE_UPDATED:
				if(employee == null && employeeGuid != null) {
					employee = employeeRepository.findByGuid(employeeGuid);
				}
				mailingSet.add(employee.getEmailId());
				break;
			case ATTACHMENT_ADDED:
			case ATTACHMENT_REMOVED:
				if(parentId != null && training == null) {
					training = trainingRepository.findById(parentId);
				}
				mailingSet = addTraineesEmailId(mailingSet);
				mailingSet = addTrainersEmailId(mailingSet);
				break;
			case COMMENT_ADDED:
			case COMMENT_REMOVED:
				training = trainingRepository.findById(parentId);
				mailingSet = addTraineesEmailId(mailingSet);
				mailingSet = addTrainersEmailId(mailingSet);
				Training trn = trainingRepository.findAllCommentsOwnerGuidById(training.getId());
				if(!CollectionUtils.isEmpty(trn.getComments()) && trn.getComments().size() > 0) {
					for (Comment cmt : trn.getComments()) {
						mailingSet.add(employeeRepository.findByGuid(cmt.getOwnerGuid()).getEmailId());
					}
				}
				break;
			case FEEDBACK_REQUEST:
				training = trainingRepository.findById(parentId);
				mailingSet = addTraineesEmailId(mailingSet);
				break;
			case TRAINING_ADDED:
			case TRAINING_UPDATED:
				if(parentId != null && training == null) {
					training = trainingRepository.findById(parentId);
				}
				mailingSet = addTraineesEmailId(mailingSet);
				mailingSet = addTrainersEmailId(mailingSet);
				employee = employeeRepository.findByGuid(training.getCreatedByGuid());
				if(employee != null) {
					mailingSet.add(employee.getEmailId());
				}
				break;
			case TOPIC_ADDED:
				employee = employeeRepository.findByGuid(topic.getCreatedByGuid());
				mailingSet.add(employee.getEmailId());
			case TOPIC_UPDATED:
				if(topic == null && parentId != null) {
					topic = topicRepository.findById(parentId);
				}
				employee = employeeRepository.findByGuid(topic.getCreatedByGuid());
				mailingSet.add(employee.getEmailId());
		}
		formatSubjectAndMsg();
		if (!StringUtils.isEmpty(msg)) {
			if (training != null) {
				Employee createdBy = employeeRepository.findByGuid(training.getCreatedByGuid());
				if (createdBy != null) {
					mailingSet.add(createdBy.getEmailId());
				}
			}
			mailingSet.remove(securedUser.getEmailId());
			this.to = mailingSet.toArray(new String[0]);
			if(this.to.length > 0) {
				mimeMessage = mailSender.createMimeMessage();
				send();
			}
		}
	}

	private Set<String> addEmployeesEmail(Set<String> employeeGuids, Set<String> emails) {
		Collection<Employee> employees = employeeRepository.findAllByGuidIn(Arrays.asList
				(employeeGuids.toArray(new String[0])));

		if (!CollectionUtils.isEmpty(employees)) {
			if (CollectionUtils.isEmpty(emails)) {
				emails = new HashSet<>(employees.size());
			}
			for (Employee employee : employees) {
				emails.add(employee.getEmailId());
			}
		}
		return emails;
	}

	private Set<String> addTrainersEmailId(Set<String> emails) {
		if(training.getTrainers() != null && training.getTrainers().size() > 0) {
			return addEmployeesEmail(training.getTrainers().keySet(), emails);
		}
		return emails;
	}

	private Set<String> addTraineesEmailId(Set<String> emails) {
		if(training.getTrainees() != null && training.getTrainees().size() > 0) {
			return addEmployeesEmail(training.getTrainees().keySet(), emails);
		}
		return emails;
	}

	private HashSet<String> getEmailsByRoles(List<String> roles) {
		HashSet<String> mailingList = new HashSet<>(roles.size());
		for (Employee employee : employeeRepository.findAllByRoles(roles)) {
			if(!StringUtils.isEmpty(employee.getEmailId())) {
				mailingList.add(employee.getEmailId());
			}
		}
		return mailingList;
	}

	private void send() {
		if (to.length > 0) {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			try {
				from = serviceAccountSettings.getEmailId();
				helper.setTo(to);
				helper.setFrom(from);
				helper.setSubject(subject);
				if (!isCalenderRequest && !StringUtils.isEmpty(msg)) {
					helper.setText(msg, true);
				}
			} catch (MessagingException e) {
				logger.error("Mail Can't be sent To: {} Subject: {} {}",
						to.toString(), subject, e);
			}
			mailSender.send(mimeMessage);
		}
	}

	private String getTrainingLink() {
		String link = MessageFormat.format(
				"{0}/#/trainings/{1}",
				applicationConfiguration.BASE_URL, training.getId().toString());
		return link;
	}

	private String getTopicLink() {
		String link = MessageFormat.format(
				"{0}/#/topics/{1}",
				applicationConfiguration.BASE_URL, topic.getId().toString());
		return link;
	}

	private String getEmployeeLink() {
		String link = MessageFormat.format(
				"{0}/#/employees/{1}",
				applicationConfiguration.BASE_URL, employee.getGuid().toUpperCase());
		return link;
	}

	private void formatSubjectAndMsg() {
		final Context ctx = new Context();// take the current locale
		String msgPage = null;

		ctx.setVariable("lunchandlearn_email",
				mailServerSettings.getEmailGroup());
		ctx.setVariable("home_page_link",
				applicationConfiguration.BASE_URL);

		if (training != null) {
			setTrainingParams(ctx);
		}
		if (employee != null) {
			setEmployeeParams(ctx);
		}
		if (topic != null) {
			setTopicParams(ctx);
		}
		switch (mailType) {
			case COMMENT_ADDED:
				subject = MessageFormat.format("{0} has commented on '{1}'", comment.getOwnerName(), training.getName());
				setCommentParams(ctx);
				msgPage = "comment_added";
				break;
			case COMMENT_REMOVED:
				subject = MessageFormat.format("{0} has deleted comment from '{1}'", comment.getOwnerName(), training.getName());
				setCommentParams(ctx);
				msgPage = "comment_deleted";
				break;
			case EMPLOYEE_ADDED:
				subject = MessageFormat.format("Added User - {0}", employee.getName());
				msgPage = "employee_added";
				break;
			case EMPLOYEE_UPDATED:
				subject = MessageFormat.format("Updated User - {0}", employee.getName());
				msgPage = "employee_updated";
				break;
			case ATTACHMENT_ADDED:
				subject = MessageFormat.format("File attached to '{0}'", training.getName());
				setFileAttachmentParams(ctx);
				msgPage = "attachment_added";
				break;
			case ATTACHMENT_REMOVED:
				subject = MessageFormat.format("File deleted from \'{0}\'", training.getName());
				setFileAttachmentParams(ctx);
				msgPage = "attachment_deleted";
				break;
			case FEEDBACK_ADDED:
				subject = MessageFormat.format("Feedback added to {0}", training.getName());
				msgPage = "feedback_added";
				break;
			case TRAINING_ADDED:
				subject = MessageFormat.format("Nominated Training - {0}", training.getName());;
				msgPage = "training_added";
				break;
			case TRAINING_UPDATED:
				subject = MessageFormat.format("Updated Training - {0}", training.getName());
				msgPage = "training_updated";
				break;
			case TOPIC_ADDED:
				subject = MessageFormat.format("Added Topic - {0}", topic.getName());
				setTopicParams(ctx);
				msgPage = "topic_added";
				break;
			case TOPIC_UPDATED:
				subject = MessageFormat.format("Updated Topic - {0}", topic.getName());
				setTopicParams(ctx);
				msgPage = "topic_updated";
				break;
		}
		msg = this.templateEngine.process(msgPage, ctx);
	}

	public void sendCalenderRequest() throws Exception {
		try {
			isCalenderRequest = true;
			Set<String> mailingSet = null;
			training = trainingRepository.findById(parentId);
			mailingSet = addTrainersEmailId(mailingSet);
//			mailingSet.addAll(ldapService.groupEmail);
			employee = employeeRepository.findByGuid(training.getCreatedByGuid());
			if(employee != null) {
				mailingSet.add(employee.getEmailId());
			}

			// Define message
			mimeMessage = mailSender.createMimeMessage();
			mimeMessage.addHeaderLine("method=" + getCalendarMethod());
			mimeMessage.addHeaderLine("charset=UTF-8");
			mimeMessage.addHeaderLine("component=VEVENT");
			//mimeMessage.setSubject(mailServerSettings.getCalenderRequestSubject());
			subject = mailServerSettings.getCalenderRequestSubject().replace("{date}",
					CommonUtil.getDayMonthWithOrdinal(training.getScheduledOn()));

			StringBuffer msgSb = new StringBuffer("\\nDear All,\\n");
			msgSb.append("This is to inform you that this week, we will conduct one session on ");
			msgSb.append(CommonUtil.getWeekMonthYear(training.getScheduledOn()));
			msgSb.append(", the details of which are given below. Pune members can be a part of the trainings through VC in Everest.\\n\\n");

			msgSb.append("Details:\\n-----------------------------\\n");
			msgSb.append(StringUtils.rightPad("Name:", 15, " ") + training.getName());
			msgSb.append("\\n");
			msgSb.append(StringUtils.rightPad("URL:", 15, " ") + getTrainingLink());
			msgSb.append("\\n");
			msgSb.append(StringUtils.rightPad("Trainer(s):", 15, " ") + collectValues(training.getTrainers()));
			msgSb.append("\\n");
			msgSb.append(StringUtils.rightPad("Time:", 15, " ") + CommonUtil.getTime(training.getScheduledOn()));
			msgSb.append("\\n");
			msgSb.append(StringUtils.rightPad("Duration:", 15, " ") + training.getDuration() + " Hours");
			if(StringUtils.isNoneEmpty(training.getWhatsForTrainees())) {
				msgSb.append("\\n\\nWhat’s in it for Trainees:\\n-----------------------------\\n");
				msgSb.append(training.getWhatsForTrainees().replace("\n", "\\n"));
			}
			if(StringUtils.isNoneEmpty(training.getWhatsForOrg())) {
				msgSb.append("\\n\\nWhat’s in it for the Organization:\\n-----------------------------\\n");
				msgSb.append(training.getWhatsForOrg().replace("\n", "\\n"));
			}
			if(StringUtils.isNoneEmpty(training.getAgenda())) {
				msgSb.append("\\n\\nAgenda:\\n-----------------------------\\n");
				msgSb.append(training.getAgenda().replace("\n", "\\n"));
				msgSb.append("\\n\\n\\nRegards,\\n");
			}
			msgSb.append("Lunch & Learn");
			msg = msgSb.toString();
			StringBuffer buffer = new StringBuffer("BEGIN:VCALENDAR\n" +
					"PRODID:-//Lunch & Learn, Pitney Bowes Software, Noida//EN\n" +
					"VERSION:2.0\n" +
					"METHOD:" + getCalendarMethod() + "\n" +
					getCalendarStatus() +
					"BEGIN:VEVENT\n" +
					"ORGANIZER:" + securedUser.getEmailId() + "\n" +
					"DTSTART:" + df.format(training.getScheduledOn()) + "\n" +
					"DTEND:" + df.format(getTrainingEndDateTime(training)) + "\n" +
					"LOCATION:" + training.getLocation() + "\n" +
					"TRANSP:OPAQUE\n" +
					"SEQUENCE:0\n" +
					"UID:" + training.getId() + "LunchAndLearn" + "\n" +
					"DTSTAMP:" + df.format(new Date()) + "\n" +
					"CATEGORIES:Internal Training\n" +
					"DESCRIPTION:" + msg + "\n\n" +
					"SUMMARY:" + training.getName() + "\n" +
					"PRIORITY:5\n" +
					"CLASS:PRIVATE\n" +
					"BEGIN:VALARM\n" +
					"TRIGGER:PT15M\n" +
					"ACTION:DISPLAY\n" +
					/*"DESCRIPTION:Reminder\n" +*/
					"END:VALARM\n" +
					"END:VEVENT\n" +
					"END:VCALENDAR");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
			messageBodyPart.setHeader("Content-ID", "calendar_message");
			messageBodyPart.setDataHandler(new DataHandler(
					new ByteArrayDataSource(buffer.toString(), "text/calendar")));// very important

			// Create a Multipart
			Multipart multipart = new MimeMultipart();

			// Add part one
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			mimeMessage.setContent(multipart);

			this.to = mailingSet.toArray(new String[0]);
			// send message
			send();
		} catch (MessagingException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getCalendarStatus() {
		switch (training.getStatus()) {
			case CANCELLED:
			case POSTPONED:
				return "STATUS:CANCELLED" + "\n";
		}
		return "";
	}

	private String getCalendarMethod() {
		switch (training.getStatus()) {
			case CANCELLED:
			case POSTPONED:
				return "CANCEL";
			case SCHEDULED:
				return "REQUEST";
		}
		return "";
	}

	private String collectValues(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		if(map != null && map.size() > 0) {
			for (String value : map.values()) {
				sb.append(value);
				if (map.size() > 1) {
					sb.append(", ");
				}
			}
		}
		return sb.toString();
	}

	private void setTrainingParams(Context ctx) {
		ctx.setVariable("employee_name", training.getCreatedByName());
		ctx.setVariable("employee_id", training.getCreatedByGuid());
		ctx.setVariable("training_reqid", training.getId());
		ctx.setVariable("training_name", training.getName());
		ctx.setVariable("training_id_link", getTrainingLink());
		if (mailType == MailService.MailType.TRAINING_ADDED) {
			ctx.setVariable("training_topics", StringUtils.join(training.getTopics().values(), ", "));
		} else if (mailType == MailService.MailType.TRAINING_UPDATED)
			if (!StringUtils.isEmpty(updatedFieldName)) {
				ctx.setVariable("training_updated_field_name", StringUtils.capitalize(updatedFieldName));
			}
			if (updatedFieldValue != null) {
				ctx.setVariable("training_updated_field_value", getValues());
			}
	}

	private String getValues() {
		if(updatedFieldValue != null) {
			if (updatedFieldValue instanceof List) {
				return StringUtils.join((List) updatedFieldValue, ", ");
			}
			else if (updatedFieldValue instanceof List) {
				return StringUtils.join((List) updatedFieldValue, ", ");
			}
			else if(updatedFieldValue instanceof Map) {
				return StringUtils.join(((Map)updatedFieldValue).values(), ", ");
			}
			return updatedFieldValue.toString();
		}
		return null;
	}
/*
	private void convertToFieldName() {
		for(int count = 0; count < updatedFieldName.length(); ++count) {
			if(updatedFieldName.charAt(count))
		}
	}
*/

	private void setEmployeeParams(Context ctx) {
		ctx.setVariable("employee_name", employee.getName());
		ctx.setVariable("employee_id", employee.getGuid());
		ctx.setVariable("employee_email", employee.getEmailId());
		ctx.setVariable("employee_id_link", getEmployeeLink());
	}

	private void setCommentParams(Context ctx) {
		ctx.setVariable("training_comment", comment.getText());
	}

	private void setTopicParams(Context ctx) {
		ctx.setVariable("topic_name", topic.getName());
		ctx.setVariable("topic_id", topic.getName());
		ctx.setVariable("employee_name", employee.getName());
		ctx.setVariable("topic_id_link", getTopicLink());
		ctx.setVariable("topic_desc", topic.getDesc());
	}

	private void setFileAttachmentParams(Context ctx) {
		ctx.setVariable("file_name", fileAttachmentInfo.getFileName());
	}

	private static Date getTrainingEndDateTime(Training training) {
		if(training.getDuration() != null) {
			return DateUtils.addMilliseconds(training.getScheduledOn(), (int) training.getDuration().floatValue() * 3600000);
		}
		return null;
	}

	@Override
	public void run() {
		switch (mailType) {
			case TRAINING_SCHEDULED:
			case TRAINING_CANCELLED:
				try {
					sendCalenderRequest();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				sendMail();
		}
	}

	public void setReceipentsGuid(Collection<String> receipentsGuid) {
		this.recipientsGuid = receipentsGuid;
	}
}