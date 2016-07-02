package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.domain.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by DE007RA on 6/6/2016.
 */
public interface CustomEmployeeRepository {

	void removeTraining(String empGuid, Long trainingId);
	void addTraining(String empGuid, Training training);

	void removeTopic(String empGuid, Long topicId);

	Map<Long, String> getTopics(String empGuid);

	List<MiniTrainingDetail> getTrainings(String empGuid);

	void addTopic(String empGuid, Long topicId, String topicName);

	boolean updateByFieldName(String empGuid, SimpleFieldEntry simpleFieldEntry);

	void updateTopics(Long topicId, String s);

	void updateTrainings(Long trainingId, String trainingName);
}