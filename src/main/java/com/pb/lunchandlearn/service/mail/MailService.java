package com.pb.lunchandlearn.service.mail;

import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.service.EmployeeService;
import com.pb.lunchandlearn.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("mailService")
public class MailService {

	private static final short THREAD_POOL_SIZE = 15;
	private ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

	@Autowired
	ApplicationContext context;

	@PreDestroy
	public void shutdown() {
		this.executor.shutdown();
	}

	public void sendMail(MailType mailType, Training training) {
		MailingTask mailingTask = context.getBean(MailingTask.class);
		mailingTask.setMailType(mailType);
		mailingTask.setTraining(training);
		executor.execute(mailingTask);
	}

	public void sendMail(MailType mailType, Employee employee) {
		MailingTask mailingTask = context.getBean(MailingTask.class);
		mailingTask.setMailType(mailType);
		mailingTask.setEmployee(employee);
		executor.execute(mailingTask);
	}

	public void sendMail(MailType mailType, Comment comment, Long trainingId) {
		MailingTask mailingTask = context.getBean(MailingTask.class);
		mailingTask.setMailType(mailType);
		mailingTask.setParentId(trainingId);
		mailingTask.setComment(comment);
		executor.execute(mailingTask);
	}

	public void sendMail(MailType mailType, FeedBack feedBack) {
		MailingTask mailingTask = context.getBean(MailingTask.class);
		mailingTask.setMailType(mailType);
		mailingTask.setFeedBack(feedBack);
		executor.execute(mailingTask);
	}

	public void sendMail(MailType mailType, FileAttachmentInfo fileInfo) {
		MailingTask mailingTask = context.getBean(MailingTask.class);
		mailingTask.setMailType(mailType);
		mailingTask.setFileAttachmentInfo(fileInfo);
		executor.execute(mailingTask);
	}

	public void sendMail(MailType mailType, Topic topic) {
		MailingTask mailingTask = context.getBean(MailingTask.class);
		mailingTask.setMailType(mailType);
		mailingTask.setTopic(topic);
		executor.execute(mailingTask);
	}

	public void sendMail(MailType mailType, Long parentId) {
		MailingTask mailingTask = context.getBean(MailingTask.class);
		mailingTask.setMailType(mailType);
		mailingTask.setParentId(parentId);
		executor.execute(mailingTask);
	}

	public void sendMail(MailType mailType, String guid) {
		MailingTask mailingTask = context.getBean(MailingTask.class);
		mailingTask.setMailType(mailType);
		mailingTask.setEmployeeGuid(guid);
		executor.execute(mailingTask);
	}

	public enum MailType {
		ATTACHMENT_ADDED, ATTACHMENT_REMOVED, COMMENT_ADDED,
		COMMENT_REMOVED, FEEDBACK_ADDED, TRAINING_ADDED, TRAINING_UPDATED,
		FEEDBACK_REQUEST, EMPLOYEE_ADDED, EMPLOYEE_UPDATED, TOPIC_ADDED,
		TOPIC_UPDATED, TRAINING_SCHEDULED, TRAINING_CANCELLED;
	}
}