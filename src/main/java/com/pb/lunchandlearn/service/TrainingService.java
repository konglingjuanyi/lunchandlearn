package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.exception.DuplicateResourceException;
import com.pb.lunchandlearn.exception.InvalidOperationException;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import com.pb.lunchandlearn.repository.FeedbackRepository;
import com.pb.lunchandlearn.repository.TrainingRepository;
import com.pb.lunchandlearn.service.mail.MailService;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Predicate;

import static com.pb.lunchandlearn.config.SecurityConfig.getLoggedInUser;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;

/**
 * Created by de007ra on 5/1/2016.
 */
@Service
public class TrainingService {
	@Autowired
	private TrainingRepository trainingRepository;

	@Autowired
	private TopicService topicService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Autowired
	private MailService mailService;

	public List<Training> getAll() {
		return trainingRepository.findAll();
	}

	public JSONObject getAll(Pageable pageable, boolean contentOnly, String trainingStatus) {
		if (StringUtils.isEmpty(trainingStatus)) {
			return getTrainingsJSON(trainingRepository.findAll(pageable), contentOnly);
		} else {
			TrainingStatus status = TrainingStatus.valueOf(trainingStatus.toUpperCase());
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
			topicService.addTrainingTo(tran.getTopics(), tran);
		}
		return tran;
	}

	public Training update(Training training) {
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
		SecuredUser user = getLoggedInUser();
		return CommonUtil.getTrainingJsonBrief(trainingRepository.updateLikes(trainingId, type, user.getUsername(), user.getGuid()));
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

	public boolean updateField(Long trainingId, SimpleFieldEntry simpleFieldEntry) throws ParseException {
		switch (simpleFieldEntry.getName()) {
			case "scheduledOn":
				if (simpleFieldEntry.getValue() != null) {
					simpleFieldEntry.setValue(CommonUtil.parseDate(simpleFieldEntry.getValue().toString()));
				}
				break;
			case "status":
				TrainingStatus status = TrainingStatus.valueOf(simpleFieldEntry.getValue().toString());
				if(!isValidStatus(trainingId, status)) {
					throw new InvalidOperationException("Status can't be set to " +status.toString());
				}
				break;
		}
		SecuredUser user = getLoggedInUser();
		if (!trainingRepository.updateByFieldName(trainingId, simpleFieldEntry, user)) {
			return false;
		}

		switch (simpleFieldEntry.getName()) {
			case "name":
				employeeService.updateTrainings(trainingId, simpleFieldEntry.getValue().toString());
				break;
			case "status":
				//update topics
				Training training = trainingRepository.findById(trainingId);
				topicService.addTrainingTo(training.getTopics(), training);
				if(TrainingStatus.COMPLETED == training.getStatus()) {
					//update trainers
					employeeService.addTrainingTo(training.getTrainers(), training, "trainingsImparted");
				}
				else if(training.getStatus() == TrainingStatus.SCHEDULED) {
					//send training invites
					//update trainees
//						employeeService.addTrainingTo(training.getTrainees(), training, "trainingsAttended");
				}
				else if(training.getStatus() == TrainingStatus.CANCELLED ||
						training.getStatus() == TrainingStatus.POSTPONED) {
					//send training invites
					mailService.sendMail(MailService.MailType.TRAINING_CANCELLED, training);
					//update trainees
//						employeeService.addTrainingTo(training.getTrainees(), training, "trainingsAttended");
				}
				break;
		}
		return true;
	}

	private boolean isValidStatus(Long trainingId, TrainingStatus statusToSet) {
		Training training = trainingRepository.getStatusById(trainingId);
		TrainingStatus status = training.getStatus();
		if(status == statusToSet) {
			return false;
		}
		if(status == TrainingStatus.COMPLETED) {
			return false;
		}
		if(statusToSet == TrainingStatus.SCHEDULED && (status != TrainingStatus.CANCELLED &&
				status != TrainingStatus.POSTPONED && status != TrainingStatus.NOMINATED)) {
			return false;
		}
		return true;
	}

	public List<Training> getTrendingTrainings() {
		return trainingRepository.findAll();
	}

	public Comment add(Comment comment, Long trainingId) {
		return trainingRepository.addComment(trainingId, setOwner(comment));
	}

	private Comment setOwner(Comment comment) {
		SecuredUser user = getLoggedInUser();
		comment.setOwnerName(user.getUsername());
		comment.setOwnerGuid(user.getGuid());
		return comment;
	}

	public Comment add(Comment comment, Long trainingId, Long parentCommentId) {
		return trainingRepository.addCommentReply(setOwner(comment), trainingId, parentCommentId);
	}

	public FileAttachmentInfo add(Long trainingId, String fileName, InputStream is) {
		if (trainingRepository.isFileAttachmentExist(trainingId, fileName)) {
			throw new DuplicateResourceException(MessageFormat.format("File " +
					"{0} already exist!", fileName));
		}
		FileAttachmentInfo fileInfo = trainingRepository.attachFile(is, fileName, trainingId);
		trainingRepository.addFileAttachmentInfo(trainingId, fileInfo);
		return fileInfo;
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

	public boolean setTrainingStatus(Long trainingId, TrainingStatus status) {
		Training training = trainingRepository.setTrainingStatus(trainingId, status);
		if (training == null) {
			throw new ResourceNotFoundException(MessageFormat.format("Training with Id: {0} does not exist", trainingId));
		} else if (training.getStatus() != status) {
			throw new InvalidOperationException(MessageFormat.format("Training can't set to status: {0}", training.getStatus().toString()));
		}
		return true;
	}

	public Map<String, String> getTraineesById(Long trainingId) {
		return trainingRepository.findTraineesById(trainingId).getTrainees();
	}

	public FeedBack add(FeedBack feedBack) {
		return feedbackRepository.insert(feedBack);
	}

	public JSONObject getFeedBacks(Long trainingId) {
		SecuredUser user = getLoggedInUser();
		boolean isAdmin = user.isAdmin();
		boolean isTrainer = false;
		boolean isTrainee = false;
		Training training = trainingRepository.findTrainersAndTraineesAndDurationById(trainingId);
		if(!isAdmin) {
			isTrainee = training.getTrainees() != null && training.getTrainees().
					containsKey(user.getGuid());
			isTrainer = training.getTrainers() != null && training.getTrainers().
					containsKey(user.getGuid());
		}
		if(isAdmin || isTrainee || isTrainer) {
			List<FeedBack> feedBacks = feedbackRepository.findAllByParentId(trainingId);
			JSONObject jsonObject = new JSONObject();
			if(CollectionUtils.isEmpty(feedBacks)) {
				return jsonObject;
			}
			if(!isAdmin && isTrainee) {
				feedBacks.removeIf(p -> p.equals(user.getGuid()));
			}
			jsonObject.put("feedbackCount", feedBacks.size());
			if(isAdmin || isTrainee) {
				jsonObject.put("content", CommonUtil.getFeedbacks(feedBacks));
			}

			if(!CollectionUtils.isEmpty(feedBacks) && (isAdmin || isTrainer)) {
				int audienceRating = getRatingByTrainees(training.getTrainees());
				int hoursRating = getRatingByHours(training.getDuration());
				setFeedBackRatingsPoint(feedBacks, training.getScheduledOn(), jsonObject);
				int feedBacksRating = (int) jsonObject.get("feedbackRating");
				float overAllRating = ((audienceRating + hoursRating) / 2) * feedBacksRating;

				jsonObject.put("hoursRating", hoursRating);
				jsonObject.put("audienceRating", audienceRating);
				jsonObject.put("overAllReward", overAllRating);
			}
			return jsonObject;
		}
		else {
			JSONObject jsonObject = new JSONObject();
			JSONArray array = CommonUtil.getFeedbacks(feedbackRepository.
					findAllByParentIdAndRespondentGuid(trainingId, user.getGuid()));
			jsonObject.put("content", array);
			return jsonObject;
		}
	}

	private void setFeedBackRatingsPoint(List<FeedBack> feedBacks, Date trainingDate, JSONObject jsonObject) {
		if (!CollectionUtils.isEmpty(feedBacks)) {
			float avg;
			int ratingSum;
			float avgSum = 0;
			float overAllAvg;
			Map<String, Integer> individualFeedbackRatings = null;
			for (FeedBack feedBack : feedBacks) {
				if(individualFeedbackRatings == null) {
					individualFeedbackRatings = new HashMap<>(feedBack.getRatings().size());
				}
				if(feedBack.getRatings() != null) {
					ratingSum = 0;
					for (Map.Entry<String, Integer> rating : feedBack.getRatings().entrySet()) {
						Integer sum = individualFeedbackRatings.get(rating.getKey());
						if(sum == null) {
							sum = 0;
						}
						individualFeedbackRatings.put(rating.getKey(), sum + rating.getValue());
						ratingSum += rating.getValue();
					}
					avg = ratingSum / feedBack.getRatings().size();
					avgSum += avg;
				}
			}
			for(Map.Entry<String, Integer> feedbackRating : individualFeedbackRatings.entrySet()) {
				jsonObject.put(feedbackRating.getKey(), (float) (feedbackRating.getValue() / feedBacks.size()));
			}
			overAllAvg = avgSum / feedBacks.size();
			if(overAllAvg >= 4.5) {
				jsonObject.put("feedbackRating", isWeekEnd(trainingDate) ? 1500 : 1000);
			}
			else if(overAllAvg > 4 && overAllAvg < 4.5) {
				jsonObject.put("feedbackRating", isWeekEnd(trainingDate) ? 1250 : 800);
			}
			else if(overAllAvg > 3.5 && overAllAvg < 4) {
				jsonObject.put("feedbackRating", isWeekEnd(trainingDate) ? 1000 : 600);
			}
			else if(overAllAvg > 3 && overAllAvg < 3.5) {
				jsonObject.put("feedbackRating", isWeekEnd(trainingDate) ? 750 : 400);
			}
			else {
				jsonObject.put("feedbackRating", 0);
			}
		}
	}

	private boolean isWeekEnd(Date trainingDate) {
		Calendar date = new GregorianCalendar();
		date.setTime(trainingDate);
		int weekDay = date.get(Calendar.DAY_OF_WEEK);
		return weekDay == SUNDAY || weekDay == SATURDAY;
	}
	private int getRatingByHours(Float duration) {
		if(duration < 1)
			return 0;
		else if(duration > 1 && duration <= 2)
			return 2;
		else if(duration > 2 && duration <= 3)
			return 3;
		else if(duration > 3 && duration <= 4)
			return 4;
		else if(duration > 4 && duration <= 6)
			return 5;
		else if(duration > 6 && duration <= 8)
			return 8;
		else {
			throw new InvalidOperationException();
		}
	}

	private int getRatingByTrainees(Map<String, String> trainees) {
		int traineesCount = trainees != null ? trainees.size() : 0;

		if(traineesCount < 10)
			return 0;
		else if(traineesCount > 10 && traineesCount <= 15)
			return 2;
		else if(traineesCount > 15 && traineesCount <= 20)
			return 3;
		else if(traineesCount > 20 && traineesCount <= 25)
			return 4;
		return 5;
	}
	public FeedBack getFeedBack(Long feedbackId) {
		return feedbackRepository.findOne(feedbackId);
	}

	public JSONObject getTrainingMinimal(Long trainingId) {
		return CommonUtil.getTrainingJsonBrief(trainingRepository.findByTheTrainingsId(trainingId));
	}
}