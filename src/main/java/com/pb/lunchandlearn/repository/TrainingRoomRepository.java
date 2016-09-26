package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.TrainingRoom;
import com.pb.lunchandlearn.domain.TrainingRoom;
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
public interface TrainingRoomRepository extends MongoRepository<TrainingRoom, Long> {
	TrainingRoom findByName(String name);
	TrainingRoom findById(Long id);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	Page<TrainingRoom> findAllBy(TextCriteria textCriteria, Pageable pageable);
}