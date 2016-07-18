package com.pb.lunchandlearn.service.mail;

import com.pb.lunchandlearn.config.*;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.repository.EmployeeRepository;
import com.pb.lunchandlearn.repository.TopicRepository;
import com.pb.lunchandlearn.repository.TrainingRepository;
import com.pb.lunchandlearn.service.EmployeeService;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
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
	private FeedBack feedBack;
	private Employee employee = null;
	private FileAttachmentInfo fileAttachmentInfo = null;
	private Topic topic;

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

	private MimeMessage mimeMessage;

	@Autowired
	private ServiceAccountSettings serviceAccountSettings;
	private String from;

	private Long parentId;
	private String employeeGuid;

	public MailService.MailType getMailType() {
		return mailType;
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

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public FeedBack getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(FeedBack feedBack) {
		this.feedBack = feedBack;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public FileAttachmentInfo getFileAttachmentInfo() {
		return fileAttachmentInfo;
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

	public String getEmployeeGuid() {
		return employeeGuid;
	}

	public void setEmployeeGuid(String employeeGuid) {
		this.employeeGuid = employeeGuid;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public MailingTask(MailService.MailType mailType, Comment comment, Long parentId) {
		this.mailType = mailType;
		this.comment = comment;
		this.parentId = parentId;
	}

	public MailingTask(MailService.MailType mailType, FeedBack feedBack) {
		this.mailType = mailType;
		this.feedBack = feedBack;
	}

	public MailingTask(MailService.MailType mailType,
					   FileAttachmentInfo fileInfo) {
		this.mailType = mailType;
		this.fileAttachmentInfo = fileInfo;
	}

	public MailingTask(MailService.MailType mailType, Long parentId) {
		this.mailType = mailType;
		this.parentId = parentId;
	}

	public MailingTask(MailService.MailType mailType, Topic topic) {
		this.mailType = mailType;
		this.topic = topic;
	}

	public MailingTask(MailService.MailType mailType, Training training) {
		this.mailType = mailType;
		this.training = training;
	}

	public MailingTask(MailService.MailType mailType, Employee employee) {
		this.mailType = mailType;
		this.employee = employee;
	}

	public MailingTask() {
	}

	private void sendMail() {
		Set<String> mailingSet = null;
		mailingSet = getEmailsByRoles(EmployeeService.ADMIN_ROLE_LIST);
		List<Employee> employees = null;
		switch (mailType) {
			case EMPLOYEE_ADDED:
				mailingSet.add(employee.getEmailId());
				break;
			case EMPLOYEE_UPDATED:
				if(employee == null) {
					employee = employeeRepository.findByGuid(employeeGuid);
				}
				mailingSet.add(employee.getEmailId());
				break;
			case ATTACHMENT_ADDED:
			case ATTACHMENT_REMOVED:
				training = trainingRepository.findById(parentId);
				employees = employeeRepository.findAllByGuidIn(Arrays.asList
						(training.getTrainees().keySet().toArray(new String[0])));
				mailingSet = addEmails(employees, mailingSet);
				if (getLoggedInUser().getGuid() != fileAttachmentInfo.getOwnerGuid()) {
					mailingSet.add(employeeRepository.findByGuid(fileAttachmentInfo.getOwnerGuid()).getEmailId());
				}
				break;
			case COMMENT_ADDED:
			case COMMENT_REMOVED:
				training = trainingRepository.findById(parentId);
				employees = employeeRepository.findAllByGuidIn(Arrays.asList
						(training.getTrainees().keySet().toArray(new String[0])));
				mailingSet = addEmails(employees, mailingSet);
				if (getLoggedInUser().getGuid() != comment.getOwnerGuid()) {
					mailingSet.add(employeeRepository.findByGuid(comment.getOwnerGuid()).getEmailId());
				}
				break;
			case FEEDBACK_ADDED:
				training = trainingRepository.findById(feedBack.getParentId());
				employees = employeeRepository.findAllByGuidIn(Arrays.asList
						(training.getTrainers().keySet().toArray(new String[0])));
				mailingSet = addEmails(employees, mailingSet);
				mailingSet.add(employeeRepository.findByGuid(feedBack.getRespondentGuid()).getEmailId());
				break;
			case FEEDBACK_REQUEST:
				training = trainingRepository.findById(parentId);
				employees = employeeRepository.findAllByGuidIn(Arrays.asList
						(training.getTrainees().keySet().toArray(new String[0])));
				mailingSet = addEmails(employees, mailingSet);
				break;
			case TRAINING_UPDATED:
				training = trainingRepository.findById(parentId);
				employees = employeeRepository.findAllByGuidIn(Arrays.asList
						(training.getTrainers().keySet().toArray(new String[0])));
				mailingSet = addEmails(employees, mailingSet);
				employee = employeeRepository.findByGuid(training.getCreatedByGuid());
				if(employee != null) {
					mailingSet.add(employee.getEmailId());
				}
				break;
			case TRAINING_ADDED:
				training = trainingRepository.findById(parentId);
				employee = employeeRepository.findByGuid(training.getCreatedByGuid());
				if(employee != null) {
					mailingSet.add(employee.getEmailId());
				}
				break;
			case TOPIC_ADDED:
				employee = employeeRepository.findByGuid(topic.getCreatedByGuid());
				mailingSet.add(employee.getEmailId());
			case TOPIC_UPDATED:
				if(topic == null) {
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
			this.to = mailingSet.toArray(new String[0]);
			mimeMessage = mailSender.createMimeMessage();
			send();
		}
	}

	private Set<String> addEmails(List<Employee> employees, Set<String> emails) {
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
				if (!StringUtils.isEmpty(msg)) {
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
				"{0}/trainings/{1}",
				applicationConfiguration.BASE_URL, training.getId());
		return link;
	}

	private String getTopicLink() {
		String link = MessageFormat.format(
				"{0}/topics/{1}",
				applicationConfiguration.BASE_URL, topic.getId());
		return link;
	}

	private String getEmployeeLink() {
		String link = MessageFormat.format(
				"{0}/employees/{1}",
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
				subject = MessageFormat.format("{0} has commented on {1}", comment.getOwnerName(), training.getName());
				setCommentParams(ctx);
				msgPage = "comment_added";
				break;
			case COMMENT_REMOVED:
				subject = MessageFormat.format("{0} has removed comment from {1}", comment.getOwnerName(), training.getName());
				setCommentParams(ctx);
				msgPage = "comment_added";
				break;
			case EMPLOYEE_ADDED:
				subject = MessageFormat.format("User {0} has been added", employee.getName());
				msgPage = "employee_added";
				break;
			case EMPLOYEE_UPDATED:
				subject = MessageFormat.format("User {0} has been updated", employee.getName());
				msgPage = "employee_updated";
				break;
			case ATTACHMENT_ADDED:
				subject = MessageFormat.format("File attached in {0}", training.getName());
				setFileAttachmentParams(ctx);
				msgPage = "attachment_added";
				break;
			case ATTACHMENT_REMOVED:
				subject = MessageFormat.format("File removed from {0}", training.getName());
				setFileAttachmentParams(ctx);
				msgPage = "attachment_removed";
				break;
			case FEEDBACK_ADDED:
				subject = MessageFormat.format("Feedback added to {0}", training.getName());
				msgPage = "feedback_added";
				break;
			case TRAINING_ADDED:
				subject = "Training nomination";
				msgPage = "training_added";
				break;
			case TRAINING_UPDATED:
			case TRAINING_SCHEDULED:
				subject = MessageFormat.format("Training {0} updated", training.getName());
				msgPage = "training_updated";
				break;
			case TOPIC_ADDED:
				subject = MessageFormat.format("Training {0} updated", training.getName());
				setTopicParams(ctx);
				msgPage = "topic_updated";
				break;
			case TOPIC_UPDATED:
				subject = MessageFormat.format("Training {0} updated", training.getName());
				setTopicParams(ctx);
				msgPage = "topic_updated";
				break;
		}
		msg = this.templateEngine.process(msgPage, ctx);
	}

	public void sendCalenderRequest() throws Exception {
		try {
			Set<String> mailingSet = null;
			training = trainingRepository.findById(parentId);
			List<Employee> employees = employeeRepository.findAllByGuidIn(Arrays.asList
					(training.getTrainers().keySet().toArray(new String[0])));
			mailingSet = addEmails(employees, mailingSet);
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
			StringBuffer sb = new StringBuffer();
			StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n" +
					"PRODID:-//Lunch & Learn, Pitney Bowes Software, Noida//EN\n" +
					"VERSION:1.0\n" +
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
					"DESCRIPTION:Reminder\n" +
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

	private void setTrainingParams(Context ctx) {
		ctx.setVariable("employee_name", training.getCreatedByName());
		ctx.setVariable("employee_id", training.getCreatedByGuid());
		ctx.setVariable("training_reqid", training.getId());
		ctx.setVariable("training_name", training.getName());
		ctx.setVariable("training_id_link", getTrainingLink());
	}

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
		ctx.setVariable("employee_name", topic.getName());
		ctx.setVariable("topic_id_link", getTopicLink());
	}

	private void setFileAttachmentParams(Context ctx) {
		ctx.setVariable("file_name", comment.getText());
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
}