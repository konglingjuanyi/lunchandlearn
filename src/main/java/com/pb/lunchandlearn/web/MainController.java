package com.pb.lunchandlearn.web;

/**
 * Created by DE007RA on 4/27/2016.
 */
import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.repository.TopicRepository;
import com.pb.lunchandlearn.service.EmployeeService;
import com.pb.lunchandlearn.service.TopicService;
import com.pb.lunchandlearn.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sections")
public class MainController {
	@Autowired
	public TrainingService trainingService;

	@Autowired
	public TopicService topicService;

	@Autowired
	public EmployeeService employeeService;

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Long> sectionsCount() {
		Map<String, Long> sectionsCount = new HashMap<String, Long>(3);
		sectionsCount.put("employees", employeeService.getCount());
		sectionsCount.put("topics", topicService.getCount());
		sectionsCount.put("trainings", trainingService.getCount());
		return sectionsCount;
//		return MessageFormat.format("'{' 'employeeCount': {0}, 'topicCount': {1}, 'trainingsCount': {2}'}'",
//				employeeService.getCount(), topicService.getCount(), trainingService.getCount());
	}

}