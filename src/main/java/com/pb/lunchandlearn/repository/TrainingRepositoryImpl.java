package com.pb.lunchandlearn.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.gridfs.GridFsCriteria.whereMetaData;

/**
 * Created by DE007RA on 6/6/2016.
 */
@Repository
public class TrainingRepositoryImpl implements CustomTrainingRepository {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private GridFsOperations gridFsOperations;

	private final String trainingCollectionName = "trainings";

	@Override
	public Training updateLikes(Long trainingId, LikeType type, String userName, String userGuid) {
		Query query = new Query(where("id").is(trainingId));
		Training training = mongoTemplate.findOne(query, Training.class);
		switch (type) {
			case LIKE:
				if (training.getLikedBy() == null) {
					training.setLikedBy(Collections.singletonMap(userGuid, userName));
				}
				else {
					training.getLikedBy().put(userGuid, userName);
				}
				break;
			case DISLIKE:
				training.getLikedBy().remove(userGuid);
				break;
		}
		training.setLikesCount(training.getLikedBy().size());
		mongoTemplate.updateFirst(query, Update.update("likedBy", training.getLikedBy()).
				set("likesCount", training.getLikesCount()), Training.class);
		return training;
	}

	@Override
	public List<Training> getAllByIds(List<Long> trainingIds) {
		return mongoTemplate.find(new Query(where("id").in(trainingIds)), Training.class);
	}

	@Override
	public boolean updateByFieldName(Long trainingId, SimpleFieldEntry simpleFieldEntry, SecuredUser user) {
		WriteResult result = mongoTemplate.updateFirst(new Query(where("id").is(trainingId)),
				Update.update(simpleFieldEntry.getName(), simpleFieldEntry.getValue()).
						set("lastModifiedByGuid", user.getGuid()).set("lastModifiedByName", user.getUsername())
						.set("lastModifiedOn", new Date()), Training.class);
		return result.getN() == 1;
	}

	@Override
	public Training findTrainingById(Long id) {
		Query query = new Query(where("id").is(id));
		query.fields().include("trainings").exclude("id");
		return mongoTemplate.findOne(query, Training.class, trainingCollectionName);
	}

	@Override
	public Training getTopicsById(Long trainingId) {
		Query query = Query.query(where("id").is(trainingId));
		query.fields().include("topics").exclude("id");
		return mongoTemplate.findOne(query, Training.class);
	}

	@Override
	public Training getCommentsById(Long trainingId) {
		Query query = Query.query(where("id").is(trainingId));
		query.fields().include("comments").exclude("id");
		return mongoTemplate.findOne(query, Training.class);
	}

	@Override
	public Comment addComment(Long trainingId, Comment comment) {
		Query query = Query.query(where("id").is(trainingId));
		Update update = new Update().push("comments", comment);
		WriteResult result = mongoTemplate.updateFirst(query, update, Training.class);
		if (result.getN() == 1) {
			return comment;
		}
		return null;
	}

	@Override
	public Comment addCommentReply(Comment comment, Long trainingId, Long parentCommentId) {
		Query query = Query.query(where("id").is(trainingId).and("comments.id").is(parentCommentId));
		Update update = new Update().push("comments.$.replies", comment);
		WriteResult result = mongoTemplate.updateFirst(query, update, Training.class);
		if (result.getN() == 1) {
			return comment;
		}
		return null;
	}

	@Override
	public boolean removeCommentReply(Long trainingId, Long commentId, Long replyCommentId) {
		Query query = Query.query(where("id").is(trainingId).and("comments.id").is(commentId));
		Update update = new Update().pull("comments.$.replies", new BasicDBObject("id", replyCommentId));
		WriteResult result = mongoTemplate.updateFirst(query, update, Training.class);
		if (result.getN() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean removeComment(Long trainingId, Long commentId) {
		Query query = Query.query(where("id").is(trainingId));
		Update update = new Update().pull("comments", new BasicDBObject("id", commentId));
		int result = mongoTemplate.updateFirst(query, update, Training.class).getN();
		if (result == 0) {
			return false;
		}
		return true;
	}

	@Override
	public FileAttachmentInfo getAttachedFileInfo(Long trainingId, String fileName) {
		Query query = Query.query(where("id").is(trainingId).and("attachmentInfos.fileName").is(fileName));
		Training info = mongoTemplate.findOne(query, Training.class);
		if (info == null) {
			return null;
		}
		return info.getAttachmentInfos().get(0);
	}

	@Override
	public Training getTrainersById(Long trainingId) {
		Query query = Query.query(where("id").is(trainingId));
		query.fields().include("topics").exclude("id");
		return mongoTemplate.findOne(query, Training.class);
	}

	@Override
	public Training setTrainingStatus(Long trainingId, TrainingStatus trainingStatus) {
		Query query = Query.query(where("id").is(trainingId));
		query.fields().include("status");
		if(mongoTemplate.updateFirst(query, new Update().set("status", trainingStatus), Training.class).getN() == 0) {
			return  null;
		}
		return mongoTemplate.findOne(query, Training.class);
	}

	@Override
	public boolean addFeedBack(FeedBack feedBack) {
		Query query = Query.query(where("id").is(feedBack.getParentId()));
		Update update = new Update().addToSet("feedbackList", feedBack.getId());
		int updateCount = mongoTemplate.updateFirst(query, update, Training.class).getN();
		if (updateCount == 1) {
			return true;
		}
		return false;
	}

	@Override
	public Comment getComment(Long trainingId, Long commentId) {
		Query query = Query.query(where("id").is(trainingId).and("comments.id").is(commentId));
		query.fields().include("comments");
		Training training = mongoTemplate.findOne(query, Training.class);
		if(CollectionUtils.isEmpty(training.getComments())) {
			return null;
		}
		for(Comment comment : training.getComments()) {
			if(comment.getId().equals(commentId)) {
				return comment;
			}
		}
		return null;
	}

	@Override
	public Comment getCommentReply(Long trainingId, Long commentId, Long replyCommentId) {
		Comment cmt = getComment(trainingId, commentId);
		if(cmt == null || CollectionUtils.isEmpty(cmt.getReplies())) {
			return null;
		}
		for(Comment reply : cmt.getReplies()) {
			if(reply.getId().equals(replyCommentId)) {
				return reply;
			}
		}
		return null;
	}

	@Override
	public FileAttachmentInfo getAttachmentFileInfoWithFile(Long trainingId, String fileName) throws IOException {
		Query query = Query.query(whereMetaData().is(new BasicDBObject("trainingId", trainingId)).
				and("filename").is(fileName));
		GridFSDBFile file = gridFsOperations.findOne(query);
		if (file == null) {
			throw new ResourceNotFoundException(MessageFormat.
					format("Attached File: {0} in Training with id: {1}", trainingId, fileName));
		}
		return new FileAttachmentInfo(file.getFilename(), file.getContentType(), file.getLength(), file.getInputStream());
	}

	@Override
	public FileAttachmentInfo attachFile(InputStream ios, String fileName, Long trainingId) {
		DBObject obj = new BasicDBObject("trainingId", trainingId);

		String contentType = null;
		GridFSFile gridFSFile = gridFsOperations.store(ios, fileName, contentType, obj);
		return new FileAttachmentInfo(gridFSFile.getFilename(), gridFSFile.getContentType(), gridFSFile.getLength());
	}

	@Override
	public boolean addFileAttachmentInfo(Long trainingId, FileAttachmentInfo fileInfo) {
		Query query = Query.query(where("id").is(trainingId));
		Update update = new Update().addToSet("attachmentInfos", fileInfo);
		int updateCount = mongoTemplate.updateFirst(query, update, Training.class).getN();
		if (updateCount == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isFileAttachmentExist(Long trainingId, String fileName) {
		Query query = Query.query(where("id").is(trainingId).and("attachmentInfos.fileName").
				is(fileName));
		return mongoTemplate.exists(query, Training.class);
	}

	@Override
	public List<FileAttachmentInfo> getAttachedFiles(Long trainingId) {
		Query query = Query.query(where("id").is(trainingId));
		query.fields().exclude("id").include("attachmentInfos.fileName").include("attachmentInfos.size");
		Training training = mongoTemplate.findOne(query, Training.class);
		if (training != null) {
			return training.getAttachmentInfos();
		}
		return null;
	}

	@Override
	public boolean removeAttachedFile(Long trainingId, String fileName) {
		Query query = Query.query(whereMetaData().is(new BasicDBObject("trainingId", trainingId)).
				and("filename").is(fileName));
		gridFsOperations.delete(query);
		Update update = new Update().pull("attachmentInfos", new BasicDBObject("fileName", fileName));
		int result = mongoTemplate.updateFirst(Query.query(where("id").is(trainingId)), update, Training.class).getN();
		if (result == 0) {
			return false;
		}
		return true;
	}
}