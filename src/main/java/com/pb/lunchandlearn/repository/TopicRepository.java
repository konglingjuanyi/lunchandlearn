package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.Topic;
import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.domain.TrainingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by de007ra on 5/1/2016.
 */
@Repository
public interface TopicRepository extends MongoRepository<Topic, String>, CustomTopicRepository {
	Topic findByName(String name);
	Topic findById(Long id);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	Page<Topic> findAllBy(TextCriteria textCriteria, Pageable pageable);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	Page<Training> findAllByStatusOrderByScore(TrainingStatus status, TextCriteria textCriteria, Pageable pageable);

	@Query(fields = "{'name': 1, 'id': 0}")
	Topic findNameById(Long topicId);

	@Query(fields = "{'likesCount': 1}")
	Topic findLikesCountById(Long trainingId);
}