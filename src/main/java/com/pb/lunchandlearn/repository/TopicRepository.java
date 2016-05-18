package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by de007ra on 5/1/2016.
 */
@Repository
public interface TopicRepository extends MongoRepository<Topic, String> {
	public Topic findByName(String name);
}
