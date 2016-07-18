package com.pb.lunchandlearn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by de007ra on 5/4/2016.
 */
@Configuration
@EnableSpringDataWebSupport
public class WebMVCConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("").setViewName("forward:/html/main/index.html");
/*		registry.addViewController("/employees/show").setViewName("forward:/html/employee/employees.html");
		registry.addViewController("/employee/show").setViewName("forward:/html/employee/employee.html");
		registry.addViewController("/trainings/show").setViewName("forward:/html/training/trainings.html");
		registry.addViewController("/training/show").setViewName("forward:/html/training/training.html");
		registry.addViewController("/topics/show").setViewName("forward:/html/topic/trainings.html");
		registry.addViewController("/topic/show").setViewName("forward:/html/topic/topic.html");
		registry.addViewController("/admin/settings/show").setViewName("forward:/html/admin_settings.html");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
*/
	}
}