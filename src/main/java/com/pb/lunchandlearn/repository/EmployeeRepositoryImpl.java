package com.pb.lunchandlearn.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by DE007RA on 6/6/2016.
 */
public class EmployeeRepositoryImpl implements CustomEmployeeRepository {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void removeTraining(String empGuid, Long trainingId, String trainingStr) {
		Query query = new Query(where("guid").is(empGuid));
		mongoTemplate.updateFirst(query, new Update().pull(trainingStr + ".id", trainingId), Employee.class);
	}

	@Override
	public void addTraining(String empGuid, Training training, String trainingStr) {
		Query query = new Query(where("guid").is(empGuid));
		MiniTrainingDetail miniTrainingDetail = new MiniTrainingDetail(training.getId(), training.getName(), training.getStatus());
		mongoTemplate.updateFirst(query, new Update().addToSet(trainingStr, miniTrainingDetail), Employee.class);
	}

	@Override
	public void removeTopicKnown(String empGuid, Long topicId) {
		Query query = new Query(where("guid").is(empGuid));
		mongoTemplate.updateFirst(query, new Update().pull("topicsKnown." + topicId, new BasicDBObject("$exist", true)), Employee.class);
	}

	@Override
	public Map<Long, String> getTopics(String empGuid, String topicStr) {
		Query query = new Query(where("guid").is(empGuid));
		query.fields().exclude("guid").include(topicStr);
		Employee emp = mongoTemplate.findOne(query, Employee.class);
		if(emp != null) {
			return emp.getTopicsKnown();
		}
		return null;
	}

	@Override
	public List<MiniTrainingDetail> getTrainings(String empGuid, String trainingStr) {
		Query query = new Query(where("guid").is(empGuid));
		query.fields().exclude("guid").include(trainingStr);
		Employee emp = mongoTemplate.findOne(query, Employee.class);
		if(emp != null) {
			return emp.getTrainingsAttended();
		}
		return null;
	}

	@Override
	public void addTopicKnown(String empGuid, Long topicId, String topicName) {
		addTopic(empGuid, topicId, topicName, "topicsKnown");
	}

	@Override
	public void addTopicInterested(String empGuid, Long topicId, String topicName) {
		addTopic(empGuid, topicId, topicName, "topicsInterestedIn");
	}

	@Override
	public void addTrainingsAttended(String empGuid, Long trainingId, String trainingName) {

	}

	@Override
	public void addTrainingsInterestedIn(String empGuid, Long trainingId, String trainingName) {

	}

	@Override
	public void addTrainingsImparted(String empGuid, Long trainingId, String trainingName) {

	}

	@Override
	public boolean updateByFieldName(String empGuid, SimpleFieldEntry simpleFieldEntry) {
		WriteResult result = mongoTemplate.updateFirst(new Query(where("guid").is(empGuid)),
				Update.update(simpleFieldEntry.getName(), simpleFieldEntry.getValue()), Employee.class);
		return result.getN() == 1;
	}

	@Override
	public void updateTopics(Long topicId, String topicName, String topicStr) {
		String topicKey = topicStr + "." + topicId;
		Query query = new Query(where(topicKey).exists(true));
		mongoTemplate.updateMulti(query, Update.update(topicKey, topicName), Employee.class);
	}

	@Override
	public void updateTrainings(Long trainingId, String trainingName, String trainingStr) {
		Query query = new Query(where(trainingStr + ".id").is(trainingId));
		mongoTemplate.updateMulti(query, Update.update(trainingStr + ".$.name", trainingName), Employee.class);
	}

	@Override
	public List<Employee> findAllByRoles(List<String> roles) {
		Query query = new Query(where("roles").in(roles));
		query.fields().include("name").include("guid");
		return mongoTemplate.find(query, Employee.class);
	}

	private void addTopic(String empGuid, Long topicId, String topicName, String topicStr) {
		Query query = new Query(where("guid").is(empGuid));
		Map<Long, String> topics = getTopics(empGuid, topicStr);
		if(topics == null) {
			topics = new HashMap<Long, String>(1);
		}
		topics.put(topicId, topicName);
		mongoTemplate.updateFirst(query, new Update().set(topicStr, topics), Employee.class);
	}
}