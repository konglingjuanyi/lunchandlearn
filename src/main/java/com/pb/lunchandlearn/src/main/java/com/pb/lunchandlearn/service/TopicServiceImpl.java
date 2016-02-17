package com.pb.lunchandlearn.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.pb.lunchandlearn.model.Topic;

/**
 * Created by DE007RA on 7/31/2015.
 */

@Service("topicService")
public class TopicServiceImpl implements TopicService {

	List<Topic> topics = new ArrayList<Topic>();
//	@Autowired
//	EmployeeRepository employeeRepository;

	@PostConstruct
	void initTopics() {
		topics.add(new Topic(1l, "Spring MVC", "Spring MVC desc"));
		topics.add(new Topic(2l, "Core Java", "Core Java desc"));
		topics.add(new Topic(3l, "AngularJS", "AngularJS desc"));
		topics.add(new Topic(4l, "Spring Boot", "Spring Boot desc"));
		topics.add(new Topic(5l, "Midev", "Midev desc"));
	}

	@Override
	public Topic create(Topic topic) {
//		Topic savedEmp = employeeRepository.save(emp);
		topics.add(topic);
		Topic savedTopic = topics.get(topics.size() - 1);
		return savedTopic;
	}

	@Override
	public Topic findById(Long id) {
		for(Topic topic : topics) {
			if(topic.getId().equals(id)) {
				return topic;
			}
		}
//		employeeRepository.findByGuid(guid);
		return null;
	}

	@Override
	public void deleteById(Long id) {
		Topic topicToDelete = null;
		for(Topic topic : topics) {
			if(topic.getId().equals(id)) {
				topicToDelete = topic;
				break;
			}
		}
		if(topicToDelete != null) {
			topics.remove(topicToDelete);
		}
	}

	@Override
	public Iterable<Topic> list() {
		return topics;
	}

	@Override
	public Topic update(Topic topic) {
		for(Topic t : topics) {
			if(t.getId().equals(topic.getId())) {
				t.setName(topic.getName());
				t.setDesc(topic.getDesc());
				return t;
			}
		}
		return null;
	}
}