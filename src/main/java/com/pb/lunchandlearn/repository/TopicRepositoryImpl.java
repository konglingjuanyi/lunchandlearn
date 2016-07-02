package com.pb.lunchandlearn.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.domain.MiniTrainingDetail;
import com.pb.lunchandlearn.domain.SimpleFieldEntry;
import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.domain.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
	public boolean updateByFieldName(Long topicId, SimpleFieldEntry simpleFieldEntry) {
		WriteResult result = mongoTemplate.updateFirst(new Query(where("id").is(topicId)),
				Update.update(simpleFieldEntry.getName(), simpleFieldEntry.getValue()), Topic.class);
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
	public void addTraining(Long topicId, Training training) {
		Query query = new Query(where("id").is(topicId));
		MiniTrainingDetail miniTrainingDetail = new MiniTrainingDetail(training.getId(), training.getName(), training.getStatus());
		mongoTemplate.updateFirst(query, new Update().addToSet("trainings", miniTrainingDetail), Topic.class);
	}
}