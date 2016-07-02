package com.pb.lunchandlearn.repository;

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
public interface TrainingRepository extends MongoRepository<Training, String>, CustomTrainingRepository {
	Training findByName(String name);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	Page<Training> findAllBy(TextCriteria textCriteria, Pageable pageable);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	Page<Training> findAllByStatusOrderByScore(TrainingStatus status, TextCriteria textCriteria, Pageable pageable);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	Page<Training> findAllByStatusOrderByScore(TrainingStatus status, Pageable pageable);

	//	@Query("")
	Training findById(Long trainingId);
	@Query(value="{ 'id' : ?0 }", fields="{ 'name' : 1}")
	Training findByTheTrainingsId(Long trainingId);
}