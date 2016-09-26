package com.pb.lunchandlearn.config;

import com.pb.lunchandlearn.service.es.ElasticSearchTask;
import com.pb.lunchandlearn.service.mail.MailingTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by de007ra on 7/17/2016.
 */
@Configuration
public class ApplicationConfiguration {

	@Value("${server.contextPath}")
	private String CONTEXT_PATH;
	@Value("${server.port}")
	public Long APPLICATION_PORT;

	@Value("${app.training.locations}")
	private String strTrainingLocations;
	public String BASE_URL = null;

	public String[] TRAINING_LOCATIONS;

	@PostConstruct
	public void init() {
		BASE_URL = "http:noide007ra-l1" + (APPLICATION_PORT == 80 ? "" : ":" + APPLICATION_PORT)
				+ CONTEXT_PATH;
		TRAINING_LOCATIONS = strTrainingLocations.split("; *");
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
}