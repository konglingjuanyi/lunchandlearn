package com.pb.lunchandlearn.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by DE007RA on 6/6/2016.
 */
public class TopicRepositoryImpl implements CustomTopicRepository {
	@Autowired
	private MongoTemplate mongoTemplate;

	private final String topicCollectionName = "topics";

	@Override
	public Topic updateLikes(Long topicId, LikeType type, String userName, String userId) {
		Query query = new Query(where("id").is(topicId));
		Topic topic = mongoTemplate.findOne(query, Topic.class);
		switch (type) {
			case LIKE:
				if(topic.getInterestedEmployees() == null) {
					topic.setInterestedEmployees(new HashMap<String, String>(1));
				}
				topic.getInterestedEmployees().put(userId, userName);
				break;
			case DISLIKE:
				topic.getInterestedEmployees().remove(userId);
				break;
		}
		topic.setLikesCount(topic.getInterestedEmployees().size());
		mongoTemplate.updateFirst(query, Update.update("interestedEmployees", topic.getInterestedEmployees()).
				set("likesCount", topic.getLikesCount()), Topic.class);
		return topic;
	}

	@Override
	public List<Topic> getAllByIds(List<Long> topicIds) {
		return mongoTemplate.find(new Query(where("id").in(topicIds)), Topic.class);
	}

	@Override
	public boolean updateByFieldName(Long topicId, SimpleFieldEntry simpleFieldEntry, SecuredUser user) {
		WriteResult result = mongoTemplate.updateFirst(new Query(where("id").is(topicId)),
				Update.update(simpleFieldEntry.getName(), simpleFieldEntry.getValue()).
						set("lastModifiedByGuid", user.getGuid()).set("lastModifiedByName",
						user.getUsername()).set("lastModifiedOn", new Date()), Topic.class);
		return result.getN() == 1;
	}

	@Override
	public Topic findTrainingsByIdAndBeforeDate(Long id, Date date) {
		Query query = new Query(where("id").is(id).
				and("trainings.scheduledOn").lt(date));
		query.fields().include("trainings");
		query.fields().exclude("id");
		return mongoTemplate.findOne(query, Topic.class, topicCollectionName);
	}

	@Override
	public Topic findTrainingsByIdAndAfterDate(Long id, Date date) {
		Query query = new Query(where("id").is(id).
				and("trainings.scheduledOn").gt(date));
		query.fields().include("trainings");
		query.fields().exclude("id");
		return mongoTemplate.findOne(query, Topic.class, topicCollectionName);
	}

	@Override
	public Topic findTrainingsById(Long id) {
		Query query = new Query(where("id").is(id));
		query.fields().include("trainings");
		query.fields().exclude("id");
		return mongoTemplate.findOne(query, Topic.class, topicCollectionName);
	}

	@Override
	public void removeTraining(Long topicId, Long trainingId) {
		Query query = new Query(where("id").is(topicId));
		mongoTemplate.updateFirst(query, new Update().pull("trainings", new BasicDBObject("id", trainingId)), Topic.class);
	}

	@Override
	public void upsertTraining(Long topicId, Training training) {
		Query query = new Query(where("id").is(topicId).and("trainings").elemMatch(where("_id").is(training.getId())));
		Topic topic = mongoTemplate.findOne(query, Topic.class);
		if(topic == null) {
			query = new Query(where("id").is(topicId));
			MiniTrainingDetail miniTrainingDetail = new MiniTrainingDetail(training.getId(), training.getName(), training.getStatus());
			mongoTemplate.updateFirst(query, new Update().addToSet("trainings", miniTrainingDetail), Topic.class);
			return;
		}
		mongoTemplate.updateFirst(query, new Update().set("trainings.$.name", training.getName())
				.set("trainings.$.status", training.getStatus()), Topic.class);
	}

	@Override
	public void removeEmployee(Long topicId, String empGuid, String employeesStr) {
		Query query = new Query(where("id").is(topicId));
		mongoTemplate.updateFirst(query, new Update().unset(employeesStr + "." + empGuid), Topic.class);
	}

	@Override
	public Topic getEmployees(Long topidId, String employeeStr) {
		Query query = new Query(where("id").is(topidId));
		query.fields().include(employeeStr);
		return mongoTemplate.findOne(query, Topic.class);
	}

	@Override
	public void addEmployee(Long topicId, Employee emp, String employeesStr) {
		Query query = new Query(where("id").is(topicId));
		Topic topic = getEmployees(topicId, employeesStr);
		if(topic == null) {
			return;
		}
		Map<String, String> employees = null;
		switch (employeesStr) {
			case "interestedEmployees":
				employees = topic.getInterestedEmployees();
				break;
			case "employeesKnowAbout":
				employees = topic.getEmployeesKnowAbout();
				break;
		}
		if(employees == null || employees.isEmpty()) {
			employees = new HashMap<>(1);
		}
		employees.put(emp.getGuid(), emp.getName());
		mongoTemplate.updateFirst(query, new Update().set(employeesStr, employees),
				Topic.class);
	}
}