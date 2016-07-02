package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.exception.DuplicateFileAttachmentException;
import com.pb.lunchandlearn.repository.TrainingRepository;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.pb.lunchandlearn.utils.CommonUtil.updateOldNewMapValues;

/**
 * Created by de007ra on 5/1/2016.
 */
@Service
public class TrainingService {
	@Autowired
	TrainingRepository trainingRepository;

	@Autowired
	TopicService topicService;

	@Autowired
	EmployeeService employeeService;

	public List<Training> getAll() {
		return trainingRepository.findAll();
	}

	public JSONObject getAll(Pageable pageable, boolean contentOnly, String trainingStatus) {
		if(trainingStatus == null) {
			return getTrainingsJSON(trainingRepository.findAll(pageable), contentOnly);
		}
		else {
			TrainingStatus status = TrainingStatus.valueOf(trainingStatus);
			return getTrainingsJSON(trainingRepository.findAllByStatusOrderByScore(status, pageable), contentOnly);
		}
	}

	public Long getCount() {
		return trainingRepository.count();
	}

	public Training getTraining(String empId) {
		return trainingRepository.findOne(empId);
	}

	public Training getTrainingByName(String name) {
		return trainingRepository.findByName(name);
	}

	public void deleteTraining(String empId) {
		trainingRepository.delete(empId);
	}

	public Training add(Training training) {
		Training tran = trainingRepository.insert(training);
		if (tran != null) {
			topicService.addTrainingTo((Map) tran.getTopics(), tran);
		}
		return tran;
	}

	public Training editTraining(Training training) {
		return trainingRepository.save(training);
	}

	public JSONObject getAllByIds(List<Long> trainingIds) {
		List<Training> trainings = trainingRepository.getAllByIds(trainingIds);
		JSONArray content = CommonUtil.getTrainingsJsonBrief(trainings.iterator());
		JSONObject obj = new JSONObject();
		obj.put("content", content);
		return obj;
	}

	public JSONObject getTopics(Long trainingId) {
		return new JSONObject(trainingRepository.getTopicsById(trainingId).getTopics());
	}

	public JSONArray getComments(Long trainingId) {
		Training training = trainingRepository.getCommentsById(trainingId);
		return CommonUtil.getComments(training.getComments());
	}

	public JSONObject search(String searchTerm, Pageable pageable, String trainingStatus) {
		TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(searchTerm);
		if (trainingStatus == null) {
			return getTrainingsJSON(trainingRepository.findAllBy(textCriteria, pageable), false);
		}
		return getTrainingsJSON(trainingRepository.findAllByStatusOrderByScore(TrainingStatus.valueOf(trainingStatus), textCriteria, pageable), false);
	}

	public static JSONObject getTrainingsJSON(Page<Training> trainings, boolean contentOnly) {
		JSONObject jsonObject = new JSONObject();
		if (!contentOnly) {
			jsonObject = CommonUtil.setPaginationInfo(trainings, jsonObject);
		}
		jsonObject.put("content", CommonUtil.getTrainingsJsonBrief(trainings.iterator()));
		return jsonObject;
	}

	public JSONObject updateLikes(Long trainingId, LikeType type) {
		return CommonUtil.getTrainingJsonBrief(trainingRepository.updateLikes(trainingId, type, "Deepak Rana", "de007ra"));
	}

	public static Pageable getRecentPageable() {
		return TopicService.getRecentPageable();
	}

	public static Pageable getTopByLikesPageable() {
		return TopicService.getTopByLikesPageable();
	}

	public Training getTrainingById(Long trainingId) {
		return trainingRepository.findById(trainingId);
	}

	public boolean editTrainingField(Long trainingId, SimpleFieldEntry simpleFieldEntry) throws ParseException {
		Map<Object, Object> oldValues = null;
		boolean isNameField = false;
		switch (simpleFieldEntry.getName()) {
			case "name":
				isNameField = true;
				break;
			case "scheduledOn":
				if (simpleFieldEntry.getValue() != null) {
					simpleFieldEntry.setValue(CommonUtil.parseDate(simpleFieldEntry.getValue().toString()));
				}
				break;
			case "topics":
				oldValues = (Map) trainingRepository.getTopicsById(trainingId).getTopics();
				updateEntries(trainingId, simpleFieldEntry.getName(), oldValues, (Map) simpleFieldEntry.getValue());
				break;
			case "trainers":
				oldValues = (Map) trainingRepository.getTrainersById(trainingId).getTrainers();
				updateEntries(trainingId, simpleFieldEntry.getName(), oldValues, (Map) simpleFieldEntry.getValue());
				break;
		}
		if (trainingRepository.updateByFieldName(trainingId, simpleFieldEntry)) {
			if (isNameField) {
				employeeService.updateTrainings(trainingId, simpleFieldEntry.getValue().toString());
			}
			return true;
		}
		return false;
	}

	private void updateEntries(Long trainingId, String fieldName, Map<Object, Object> oldEntries, Map<Object, Object> newEntries) {
		Map<Object, Object> addedEntries = null;
		addedEntries = updateOldNewMapValues(oldEntries, newEntries, addedEntries);
		switch (fieldName) {
			case "topics":
				if (oldEntries != null && oldEntries.size() > 0) {
					topicService.removeTrainingFrom((Map) oldEntries, trainingId);
				}
				if (addedEntries != null && addedEntries.size() > 0) {
					topicService.addTrainingTo((Map) addedEntries, trainingRepository.findByTheTrainingsId(trainingId));
				}
				break;
			case "trainers":
				if (oldEntries != null && oldEntries.size() > 0) {
					employeeService.removeTrainingFrom((Map) oldEntries, trainingId);
				}
				if (addedEntries != null && addedEntries.size() > 0) {
					employeeService.addTrainingTo((Map) addedEntries, trainingRepository.findByTheTrainingsId(trainingId));
				}
				break;
		}
	}

	public List<Training> getTrendingTrainings() {
		return trainingRepository.findAll();
	}

	public Comment addComment(Comment comment, Long trainingId) {
		return trainingRepository.addComment(trainingId, setOwner(comment));
	}

	private Comment setOwner(Comment comment) {
		comment.setOwnerName("Deepak Rana");
		comment.setOwnerGuid("de007ra");
		return comment;
	}

	public Comment addCommentReply(Comment comment, Long trainingId, Long parentCommentId) {
		return trainingRepository.addCommentReply(setOwner(comment), trainingId, parentCommentId);
	}

	public boolean attachFile(Long trainingId, String fileName, InputStream is) {
		if (trainingRepository.isFileAttachmentExist(trainingId, fileName)) {
			throw new DuplicateFileAttachmentException(MessageFormat.format("File " +
					"{0} already exist!", fileName));
		}
		FileAttachmentInfo fileInfo = trainingRepository.attachFile(is, fileName, trainingId);
		return trainingRepository.addFileAttachmentInfo(trainingId, fileInfo);
	}

	public JSONArray getAttachedFiles(Long trainingId) {
		List<FileAttachmentInfo> fileInfos = trainingRepository.getAttachedFiles(trainingId);
		return CommonUtil.getFileAttachmentInfosBrief(fileInfos);
	}

	public boolean removeAttachedFile(Long trainingId, String fileName) {
		return trainingRepository.removeAttachedFile(trainingId, fileName);
	}

	public boolean removeComment(Long trainingId, Long commentId) {
		return trainingRepository.removeComment(trainingId, commentId);
	}

	public boolean removeCommentReply(Long trainingId, Long commentId, Long replyCommentId) {
		return trainingRepository.removeCommentReply(trainingId, commentId, replyCommentId);
	}

	public FileAttachmentInfo getAttachmentFileInfo(Long trainingId, String fileName) throws IOException {
		return trainingRepository.getAttachedFileInfo(trainingId, fileName);
	}

	public FileAttachmentInfo getAttachmentFileInfoWithFile(Long trainingId, String fileName) throws IOException {
		return trainingRepository.getAttachmentFileInfoWithFile(trainingId, fileName);
	}
}