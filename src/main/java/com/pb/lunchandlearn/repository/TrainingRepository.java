package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.domain.TrainingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by de007ra on 5/1/2016.
 */
public interface TrainingRepository extends MongoRepository<Training, String>, CustomTrainingRepository {
	Training findByName(String name);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1, 'scheduledOn': 1, 'location': 1, 'duration': 1, 'status': 1}")
	Page<Training> findAllBy(TextCriteria textCriteria, Pageable pageable);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1, 'scheduledOn': 1, 'location': 1, 'duration': 1, 'status': 1}")
	List<Training> findAllBy(TextCriteria textCriteria);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	Page<Training> findAllByStatusOrderByScore(TrainingStatus status, TextCriteria textCriteria, Pageable pageable);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	List<Training> findAllByStatusOrderByScore(TrainingStatus status, TextCriteria textCriteria);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	Page<Training> findAllByStatusOrderByScore(TrainingStatus status, Pageable pageable);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1}")
	List<Training> findAllByStatusOrderByScore(TrainingStatus status);

	@Query(fields = "{'comments': 0, 'attachmentInfos': 0, 'feedBackList': 0, 'score': 0}")
	Training findById(Long trainingId);

	@Query(value="{ 'id' : ?0 }", fields="{ 'name' : 1, likesCount: 1, 'status': 1}")
	Training findByTheTrainingsId(Long trainingId);

	@Query(fields = "{'trainees': 1}")
	Training findTraineesById(Long trainingId);

	Training findTrainersAndTraineesAndDurationById(Long trainingId);

	@Query(fields = "{'status': 1}")
	Training getStatusById(Long trainingId);

	@Query(fields = "{'comments.ownerGuid': 1}")
	Training findAllCommentsOwnerGuidById(Long trainingId);

	@Query(fields = "{'likesCount': 1}")
	Training findLikesCountById(Long trainingId);

	@Query(fields = "{'name': 1, 'scheduledOn': 1, 'location': 1, 'status': 1, trainers: 1, scheduledOn: 1, trainees: 1" +
			", createdByGuid: 1}")
	List<Training> findAllByStatusAndScheduledOnBetween(TrainingStatus scheduled, Date startDate, Date endDate);

	@Query(fields = "{'name': 1, 'likesCount': 1, 'score': 1, 'scheduledOn': 1, 'location': 1, 'duration': 1, 'status': 1}")
	Page<Training> findAllByScheduledOnBetween(Date startDate, Date endDate, Pageable pageable);

	@Query(fields = "{'scheduledOn': 1}")
	Training findTopByOrderByScheduledOnDesc();

	@Query(fields = "{'scheduledOn': 1}")
	Training findTopByOrderByScheduledOnAsc();
}