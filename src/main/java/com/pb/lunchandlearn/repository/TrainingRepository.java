package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.domain.Training;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by de007ra on 5/1/2016.
 */
@Repository
public interface TrainingRepository extends MongoRepository<Training, String> {
	public Training findByName(String name);
}
