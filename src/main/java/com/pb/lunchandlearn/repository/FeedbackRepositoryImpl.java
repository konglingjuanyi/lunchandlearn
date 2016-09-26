package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.FeedBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by DE007RA on 6/6/2016.
 */
@Repository
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