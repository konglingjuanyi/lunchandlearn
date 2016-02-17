package com.pb.lunchandlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pb.lunchandlearn.model.Topic;
import com.pb.lunchandlearn.service.TopicService;

/**
 * Created by DE007RA on 7/31/2015.
 */
@RestController("topicController")
@RequestMapping("topics")
public class TopicController {

	@Autowired
	TopicService topicService;

	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean add(@RequestBody Topic topic) {
		System.out.println("Topic To add: " + topic.toString());
		Boolean status = topicService.create(topic) != null;
		return status;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean update(@RequestBody Topic topic) {
		System.out.println("Topic To update: " + topic.toString());
		Boolean status = topicService.update(topic) != null;
		return status;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Topic get(@PathVariable(value = "id") Long id) {
		Topic topic = topicService.findById(id);
		return topic;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<Topic> list() {
		return topicService.list();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean remove(@PathVariable("id") Long id) {
		Boolean status = Boolean.TRUE;
		System.out.println("Topic To Del: " + id.toString());
		topicService.deleteById(id);
		return status;
	}
}