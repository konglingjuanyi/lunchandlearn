package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.domain.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by DE007RA on 8/9/2016.
 */
public interface TrainingService {
	static boolean isTrainerField(String fieldName) {
		return fieldName == "trainers";
	}

	List<Training> getAll();

	JSONObject getAll(Pageable pageable, boolean contentOnly, String trainingStatus);

	Long getCount();

	Training getTraining(String empId);

	Training getTrainingByName(String name);

	void deleteTraining(String empId);

	Training add(Training training);

	Training update(Training training);

	JSONObject getAllByIds(List<Long> trainingIds);

	JSONObject getTopics(Long trainingId);

	JSONArray getComments(Long trainingId);

	JSONObject search(String searchTerm, Pageable pageable, String trainingStatus);

	JSONObject updateLikes(Long trainingId, LikeType type);

	Training getTrainingById(Long trainingId);

	boolean updateField(Long trainingId, SimpleFieldEntry simpleFieldEntry) throws ParseException;

	List<Training> getTrendingTrainings();

	Comment add(Comment comment, Long trainingId);

	Comment add(Comment comment, Long trainingId, Long parentCommentId);

	FileAttachmentInfo add(Long trainingId, String fileName, InputStream is);

	JSONArray getAttachedFiles(Long trainingId);

	boolean removeAttachedFile(Long trainingId, String fileName);

	boolean removeComment(Long trainingId, Long commentId);

	boolean removeCommentReply(Long trainingId, Long commentId, Long replyCommentId);

	Comment getComment(Long trainingId, Long commentId);

	Comment getCommentReply(Long trainingId, Long commentId, Long replyCommentId);

	FileAttachmentInfo getAttachmentFileInfo(Long trainingId, String fileName) throws IOException;

	FileAttachmentInfo getAttachmentFileInfoWithFile(Long trainingId, String fileName) throws IOException;

	boolean setTrainingStatus(Long trainingId, TrainingStatus status);

	Map<String, String> getTraineesById(Long trainingId);

	FeedBack add(FeedBack feedBack);

	JSONObject getFeedBacks(Long trainingId);

	FeedBack getFeedBack(Long feedbackId);

	JSONObject getTrainingMinimal(Long trainingId);

	String[] getTrainingLocations();

	Integer getTrainingLikesCount(Long trainingId);

	boolean sendFeedbackRequest(Long trainingId);

	boolean sendTrainingRequest(Long trainingId);
}
