package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.domain.SimpleFieldEntry;
import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.domain.Training;

import java.util.Date;
import java.util.List;

/**
 * Created by DE007RA on 6/6/2016.
 */
public interface CustomTopicRepository {
	Topic updateLikes(Long topicId, LikeType type, String userName, String userId);
	List<Topic> getAllByIds(List<Long> topicIds);
	boolean updateByFieldName(Long topicId, SimpleFieldEntry simpleFieldEntry, SecuredUser user);

	Topic findTrainingsByIdAndBeforeDate(Long id, Date date);

	Topic findTrainingsByIdAndAfterDate(Long id, Date date);

	Topic findTrainingsById(Long id);

	void removeTraining(Long topicId, Long trainingId);

	void upsertTraining(Long topicId, Training training);

	void removeEmployee(Long topicId, String empGuid, String employeesKnowAbout);

	Topic getEmployees(Long topidId, String employeeStr);

	void addEmployee(Long key, Employee emp, String employeesStr);
}