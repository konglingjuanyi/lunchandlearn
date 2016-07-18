package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.FeedBack;
import com.pb.lunchandlearn.domain.FeedBack;
import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.domain.TrainingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by de007ra on 5/1/2016.
 */
@Repository
public interface FeedbackRepository extends MongoRepository<FeedBack, Long> {

	@Query(fields = "{'id': 1, 'respondentGuid': 1, 'respondentName': 1, 'ratings': 1}")
	List<FeedBack> findAllByParentId(Long parentId);

	Long countByParentIdAndRespondentGuid(Long parentId, String respondentGuid);

	List<FeedBack> findAllByParentIdAndRespondentGuid(Long trainingId, String guid);
/*
	FeedBack findByName(String name);
	FeedBack findById(Long id);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	Page<FeedBack> findAllBy(TextCriteria textCriteria, Pageable pageable);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	Page<Training> findAllByStatusOrderByScore(TrainingStatus status, TextCriteria textCriteria, Pageable pageable);

	@Query(fields = "{'name': 1, 'id': 0}")
	FeedBack findNameById(Long topicId);
*/
}