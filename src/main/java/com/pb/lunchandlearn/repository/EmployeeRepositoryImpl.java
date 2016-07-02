package com.pb.lunchandlearn.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.domain.MiniTrainingDetail;
import com.pb.lunchandlearn.domain.SimpleFieldEntry;
import com.pb.lunchandlearn.domain.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by DE007RA on 6/6/2016.
 */
public class EmployeeRepositoryImpl implements CustomEmployeeRepository {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void removeTraining(String empGuid, Long trainingId) {
		Query query = new Query(where("guid").is(empGuid));
		mongoTemplate.updateFirst(query, new Update().pull("trainings.id", trainingId), Employee.class);
	}

	@Override
	public void addTraining(String empGuid, Training training) {
		Query query = new Query(where("guid").is(empGuid));
		MiniTrainingDetail miniTrainingDetail = new MiniTrainingDetail(training.getId(), training.getName(), training.getStatus());
		mongoTemplate.updateFirst(query, new Update().addToSet("trainings", miniTrainingDetail), Employee.class);
	}

	@Override
	public void removeTopic(String empGuid, Long topicId) {
		Query query = new Query(where("guid").is(empGuid));
		mongoTemplate.updateFirst(query, new Update().pull("topics." + topicId, new BasicDBObject("$exist", true)), Employee.class);
	}

	@Override
	public Map<Long, String> getTopics(String empGuid) {
		Query query = new Query(where("guid").is(empGuid));
		query.fields().exclude("guid").include("topics");
		Employee emp = mongoTemplate.findOne(query, Employee.class);
		if(emp != null) {
			return emp.getTopicsKnown();
		}
		return null;
	}

	@Override
	public List<MiniTrainingDetail> getTrainings(String empGuid) {
		Query query = new Query(where("guid").is(empGuid));
		query.fields().exclude("guid").include("trainings");
		Employee emp = mongoTemplate.findOne(query, Employee.class);
		if(emp != null) {
			return emp.getTrainingsTaken();
		}
		return null;
	}

	@Override
	public void addTopic(String empGuid, Long topicId, String topicName) {
		Query query = new Query(where("guid").is(empGuid));
		Map<Long, String> topics = getTopics(empGuid);
		if(topics == null) {
			topics = new HashMap<Long, String>(1);
		}
		topics.put(topicId, topicName);
		mongoTemplate.updateFirst(query, new Update().set("topics", topics), Employee.class);
	}

	@Override
	public boolean updateByFieldName(String empGuid, SimpleFieldEntry simpleFieldEntry) {
		WriteResult result = mongoTemplate.updateFirst(new Query(where("guid").is(empGuid)),
				Update.update(simpleFieldEntry.getName(), simpleFieldEntry.getValue()), Employee.class);
		return result.getN() == 1;
	}

	@Override
	public void updateTopics(Long topicId, String topicName) {
		String topicKey = "topic." + topicId;
		Query query = new Query(where(topicKey).exists(true));
		mongoTemplate.updateMulti(query, Update.update(topicKey, topicName), Employee.class);
	}

	@Override
	public void updateTrainings(Long trainingId, String trainingName) {
		Query query = new Query(where("trainings.id").is(trainingId));
		mongoTemplate.updateMulti(query, Update.update("trainings.$.name", trainingName), Employee.class);
	}
}