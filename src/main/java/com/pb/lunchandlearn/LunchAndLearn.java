package com.pb.lunchandlearn;

/**
 * Created by DE007RA on 4/27/2016.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class LunchAndLearn extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		builder.sources(LunchAndLearn.class);
		return builder;
	}

	public static void main(String[] args) {
		SpringApplication.run(LunchAndLearn.class, args);
	}
}