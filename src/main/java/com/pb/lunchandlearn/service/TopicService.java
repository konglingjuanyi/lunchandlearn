package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by DE007RA on 8/9/2016.
 */
public interface TopicService {
	JSONObject getAll(Pageable pageable, boolean contentOnly);

	Long getCount();

	Topic getTopicByName(String topicName);

	Topic getTopicById(Long topicId);

	void deleteTopic(String topicId);

	Topic add(Topic topic);

	Topic update(Topic topic);

	List<Topic> getTrendingTopics();

	JSONObject search(String term, Pageable pageable);

	JSONObject updateLikes(Long topicId, LikeType type);

	JSONObject getAllByIds(List<Long> topicIds);

	boolean updateField(Long topicId, SimpleFieldEntry simpleFieldEntry);

	JSONArray getTrainings(Long topicId);

	JSONArray getTrainings(Long topicId, String type);

	void addTrainingTo(Map<Long, String> topics, Training training);

	void removeTrainingFrom(Map<Long, String> topics, Long trainingId);

	void removeEmployees(Map<Long, String> topics, String empGuid, String employeesStr);

	void addEmployees(Map<Object, String> topics, Employee emp, String employeesStr);

	Integer getTrainingLikesCount(Long trainingId);

	JSONObject getSuggestedTopics(String topicName);
}
