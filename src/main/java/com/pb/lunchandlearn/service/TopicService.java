package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by de007ra on 5/1/2016.
 */
@Service
public class TopicService {
	@Autowired
	TopicRepository topicRepository;

	public List<Topic> getAll() {
		return topicRepository.findAll();
	}

	public Long getCount() {
		return topicRepository.count();
	}

public Topic getTopic(String empId) {
		return topicRepository.findOne(empId);
	}

	public Topic getTopicByName(String name) {
		return topicRepository.findByName(name);
	}

	public void deleteTopic(String empId) {
		topicRepository.delete(empId);
	}

	public Topic add(Topic employee) {
		return topicRepository.insert(employee);
	}

	public Topic editTopic(Topic employee) {
		return topicRepository.save(employee);
	}

	public List<Topic> getTrendingTopics() {
		return topicRepository.findAll();
	}
}
