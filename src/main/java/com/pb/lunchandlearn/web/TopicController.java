package com.pb.lunchandlearn.web;

/**
 * Created by DE007RA on 4/27/2016.
 */
import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {
	@Autowired
	public TopicService topicService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Topic> home() {
		return topicService.getAll();
	}

	@RequestMapping(value="/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long getCount() {
		return topicService.getCount();
	}

	@RequestMapping(value="/topic/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Topic getTopic(@PathVariable("id") String empId) {
		return topicService.getTopic(empId);
	}

	@RequestMapping(value="/topic", method = RequestMethod.POST)
	public Topic addTopic(@Valid Topic topic, BindingResult result) {
		if(!result.hasErrors()) {
			return topicService.add(topic);
		}
		return null;
	}

	@RequestMapping(value="/topic", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void editTopic(@Valid Topic topic) {
		topicService.editTopic(topic);
	}

	@RequestMapping(value="/topic/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTopic(@PathVariable("id") String empId) {
		topicService.deleteTopic(empId);
	}

	@RequestMapping(value="/trending", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Topic> trendingTopics() {
		return topicService.getTrendingTopics();
	}
}