package com.pb.lunchandlearn.web;

/**
 * Created by DE007RA on 4/27/2016.
 */
import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sections")
public class MainController {
	@Autowired
	private TrainingService trainingService;

	@Autowired
	private TopicService topicService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private TrainingRoomService trainingRoomService;

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Long> sectionsCount(@AuthenticationPrincipal SecuredUser user) {
		Map<String, Long> sectionsCount = new HashMap<String, Long>(3);
		sectionsCount.put("topics", topicService.getCount());
		sectionsCount.put("trainings", trainingService.getCount());
		if(user.isAdmin()) {
			sectionsCount.put("employees", employeeService.getCount());
			sectionsCount.put("trainingRooms", trainingRoomService.getCount());
		}
		else if(user.isManager()) {
			sectionsCount.put("employees", employeeService.countByManagers(user.getGuid()));
		}
		return sectionsCount;
	}

}