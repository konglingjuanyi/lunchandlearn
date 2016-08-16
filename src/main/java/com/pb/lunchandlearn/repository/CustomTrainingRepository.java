package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.domain.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by DE007RA on 6/6/2016.
 */
public interface CustomTrainingRepository {
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

	public boolean removeCommentReply(Long trainingId, Long commentId, Long commentReplyId);

	public boolean removeComment(Long trainingId, Long commentId);

	FileAttachmentInfo getAttachedFileInfo(Long trainingId, String fileName);

	Training getTrainersById(Long trainingId);

	Training setTrainingStatus(Long trainingId, TrainingStatus trainingStatus);

	boolean addFeedBack(FeedBack feedBack);

	Comment getComment(Long trainingId, Long commentId);

	Comment getCommentReply(Long trainingId, Long commentId, Long replyCommentId);
}