package com.pb.lunchandlearn.config;

import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.service.es.ElasticSearchTask;
import com.pb.lunchandlearn.service.mail.MailingTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by de007ra on 7/17/2016.
 */
@Configuration
@EnableScheduling
public class ApplicationConfiguration {

	@Value("${server.contextPath}")
	private String CONTEXT_PATH;
	@Value("${server.port}")
	public Long APPLICATION_PORT;

	public static String BASE_URL = null;

	@PostConstruct
	public void init() throws UnknownHostException {
		BASE_URL = "http://" + InetAddress.getLocalHost().getHostName() + (APPLICATION_PORT == 80 ? "" : ":" + APPLICATION_PORT)
				+ CONTEXT_PATH;
	}

	@Bean(name = "mailingTask")
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public static MailingTask mailingTask() {
		return new MailingTask();
	}

	@Bean(name = "elasticSearchTask")
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public static ElasticSearchTask elasticSearchTask() {
		return new ElasticSearchTask();
	}

	public static String getTopicLink(Topic topic) {
		String link = MessageFormat.format(
				"{0}/#/topics/{1}",
				BASE_URL, topic.getId().toString());
		return link;
	}

	public static String getEmployeeLink(Employee employee) {
		String link = MessageFormat.format(
				"{0}/#/employees/{1}",
				BASE_URL, employee.getGuid().toUpperCase());
		return link;
	}

	public static String getTrainingLink(Training training) {String link = MessageFormat.format("{0}/#/trainings/{1}",
			BASE_URL, training.getId().toString());
		return link;
	}

	public static String getTrainingsLink() {String link = MessageFormat.format("{0}/#/trainings",
			BASE_URL);
		return link;
	}
}