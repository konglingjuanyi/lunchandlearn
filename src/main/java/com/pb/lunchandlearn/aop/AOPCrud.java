package com.pb.lunchandlearn.aop;

import com.pb.lunchandlearn.config.ModalCollectionSettings;
import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.repository.IDRepository;
import com.pb.lunchandlearn.service.IDProviderService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by de007ra on 5/4/2016.
 */
@Component
@Aspect
public class AOPCrud {
	Logger logger = LoggerFactory.getLogger(AOPCrud.class);
	@Autowired
	IDRepository repository;
	@Autowired
	IDProviderService idProviderService;

	@Autowired
	ModalCollectionSettings modalCollectionSettings;

	@Before("execution(* com.pb.lunchandlearn.service.*Service.add(..))")
	public void setId(JoinPoint joinPoint) {
		logger.debug("Before Advicing from AOPCrud.setId");
		if(joinPoint.getTarget() instanceof Topic) {
			((Topic)joinPoint.getArgs()[0]).setId(
					idProviderService.getNextId(modalCollectionSettings.getTopic()));
		}
		else if(joinPoint.getTarget() instanceof Training) {
			((Topic)joinPoint.getArgs()[0]).setId(
					idProviderService.getNextId(modalCollectionSettings.getTraining()));
		}
	}
}
