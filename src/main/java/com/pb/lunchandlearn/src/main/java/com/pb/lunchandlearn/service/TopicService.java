package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.model.Topic;

public interface TopicService {
	Topic create(Topic topic);
	Topic findById(Long id);
	void deleteById(Long id);
	Iterable<Topic> list();
	Topic update(Topic topic);
}