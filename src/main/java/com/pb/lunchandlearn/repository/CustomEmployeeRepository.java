package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by DE007RA on 6/6/2016.
 */
public interface CustomEmployeeRepository {

	void removeTraining(String empGuid, Long trainingId, String trainingStr);
	void addTraining(String empGuid, Training training, String trainingStr);

	void removeTopicKnown(String empGuid, Long topicId);

	Map<Long, String> getTopics(String empGuid, String topicStr);

	List<MiniTrainingDetail> getTrainings(String empGuid, String trainingStr);

	void addTopicKnown(String empGuid, Long topicId, String topicName);

	void addTopicInterested(String empGuid, Long topicId, String topicName);

	void addTrainingsAttended(String empGuid, Long trainingId, String trainingName);

	void addTrainingsInterestedIn(String empGuid, Long trainingId, String trainingName);

	void addTrainingsImparted(String empGuid, Long trainingId, String trainingName);

	boolean updateByFieldName(String empGuid, SimpleFieldEntry simpleFieldEntry);

	void updateTopics(Long topicId, String topicName, String topicStr);

	void updateTrainings(Long trainingId, String trainingName, String trainingStr);

	List<Employee> findAllByRoles(List<String> roles);

	void updateAllDifferentFields(Employee emp);

	Long countByManagers(String guid);

	Page<Employee> findAllExistsByManagers(String managerGuid, Pageable pageable);

	void removeEmployees(List<String> employeesGuid);
}