package com.pb.lunchandlearn.config;

import com.pb.lunchandlearn.service.mail.MailingTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Created by de007ra on 7/17/2016.
 */
@Configuration
public class ApplicationConfiguration {

	@Value("${server.contextPath}")
	private String CONTEXT_PATH;
	@Value("${server.port}")
	public Long APPLICATION_PORT;

	public String BASE_URL = null;

	@PostConstruct
	public void init() {
		BASE_URL = "http:/" + (APPLICATION_PORT == 80 ? "" : ":" + APPLICATION_PORT)
				+ CONTEXT_PATH;
	}

	@Bean(name = "mailingTask")
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public static MailingTask mailingTask() {
		return new MailingTask();
	}
}