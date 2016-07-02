package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.domain.SimpleFieldEntry;
import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.domain.Training;

import java.util.Date;
import java.util.List;

/**
 * Created by DE007RA on 6/6/2016.
 */
public interface CustomTopicRepository {
	Topic updateLikes(Long topicId, LikeType type, String userName, String userEmailId);
	List<Topic> getAllByIds(List<Long> topicIds);
	boolean updateByFieldName(Long topicId, SimpleFieldEntry simpleFieldEntry);

	Topic findTrainingsByIdAndBeforeDate(Long id, Date date);

	Topic findTrainingsByIdAndAfterDate(Long id, Date date);

	Topic findTrainingsById(Long id);

	void removeTraining(Long topicId, Long trainingId);

	void addTraining(Long topicId, Training training);
}