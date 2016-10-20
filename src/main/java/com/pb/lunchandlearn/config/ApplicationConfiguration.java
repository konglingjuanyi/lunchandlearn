package com.pb.lunchandlearn.config;

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

	public String BASE_URL = null;

	@PostConstruct
	public void init() throws UnknownHostException {
		BASE_URL = InetAddress.getLocalHost().getHostName() + (APPLICATION_PORT == 80 ? "" : ":" + APPLICATION_PORT)
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
}