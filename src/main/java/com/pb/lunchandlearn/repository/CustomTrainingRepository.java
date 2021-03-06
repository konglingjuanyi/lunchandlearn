package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.domain.*;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by DE007RA on 6/6/2016.
 */
interface CustomTrainingRepository {
	Training updateLikes(Long trainingId, LikeType type, String userName, String userGuid);
	List<Training> getAllByIds(List<Long> topicIds);
	boolean updateByFieldName(Long trainingId, SimpleFieldEntry simpleFieldEntry, SecuredUser user);

	Training findTrainingById(Long id);

	Training getTopicsById(Long trainingId);

	Training getCommentsById(Long trainingId);

	Comment addComment(Long trainingId, Comment comment);

	Comment addCommentReply(Comment comment, Long trainingId, Long parentCommentId);

	FileAttachmentInfo getAttachmentFileInfoWithFile(Long trainingId, String fileName) throws IOException;

	FileAttachmentInfo attachFile(InputStream ios, String fileName, Long trainingId);

	boolean addFileAttachmentInfo(Long trainingId, FileAttachmentInfo fileInfo);

	boolean isFileAttachmentExist(Long trainingId, String fileName);

	List<FileAttachmentInfo> getAttachedFiles(Long trainingId);

	boolean removeAttachedFile(Long trainingId, String fileName);

	boolean removeCommentReply(Long trainingId, Long commentId, Long commentReplyId);

	boolean removeComment(Long trainingId, Long commentId);

	FileAttachmentInfo getAttachedFileInfo(Long trainingId, String fileName);

	Training getTrainersById(Long trainingId);

	Training setTrainingStatus(Long trainingId, TrainingStatus trainingStatus);

	boolean addFeedBack(FeedBack feedBack);

	Comment getComment(Long trainingId, Long commentId);

	Comment getCommentReply(Long trainingId, Long commentId, Long replyCommentId);

	Page<Training> findAllByFilter(JSONObject filterBy, TextCriteria textCriteria, Pageable pageable);

	List<Training> findAllByFilter(JSONObject filterBy, TextCriteria textCriteria);
}