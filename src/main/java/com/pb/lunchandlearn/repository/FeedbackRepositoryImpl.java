package com.pb.lunchandlearn.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.pb.lunchandlearn.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by DE007RA on 6/6/2016.
 */
public class FeedbackRepositoryImpl implements CustomFeedbackRepository {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public FeedBack getRatings(Long trainingId) {
/*
		Aggregation aggregation = Aggregation.newAggregation(FeedBack.class,
				 Aggregation.match(where("parentId").is(trainingId)), Aggregation.group("parentId")
						.avg("trainingId").as("average"));
*/

//		mongoTemplate.
		return null;
	}
}