package com.pb.lunchandlearn.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by de007ra on 4/28/2016.
 */
@Document(collection = "trainings")
public final class Training {
	@Id
	private Long id;

	@NotNull
	@Min(3)
	private String name;

	@Indexed
	@TextIndexed
	private String lowername;

	private Map<String, String> likedBy;//empId, name

	@Max(500)
	@TextIndexed
	private String desc;

	@TextScore
	private Float score;

	@NotNull
	private Map<String, String> trainers;//empId, name

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date scheduledOn;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date createDateTime;

	private Map<String, String> trainees;//empId, name

	private Map<Long, String> topics;//topicId, name

	private Map<Long, String> prerequisites;//topicId, name

	@NotNull
	private String createdByGuid;
	@NotNull
	private String createdByName;

	private String lastModifiedByGuid;
	private String lastModifiedByName;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date lastModifiedOn;

	private List<Comment> comments;

	private List<FileAttachmentInfo> attachmentInfos;

	private String agenda;

	@NotNull
	@Indexed
	private TrainingStatus status;

	private List<Long> feedBackList;

	private boolean feedbackClosed;

	private Float duration;

	private String location;

	@Indexed
	private Integer likesCount;

	private String whatsForTrainees;
	private String whatsForOrg;

	@Override
	public String toString() {
		return "Training{" +
				"id=" + id +
				", name='" + name + '\'' +
				", likedBy=" + likedBy +
				", desc='" + desc + '\'' +
				", score=" + score +
				", trainers=" + trainers +
				", scheduledOn=" + scheduledOn +
				", createDateTime=" + createDateTime +
				", trainees=" + trainees +
				", topics=" + topics +
				", prerequisites=" + prerequisites +
				", createdByGuid='" + createdByGuid + '\'' +
				", createdByName='" + createdByName + '\'' +
				", lastModifiedByGuid='" + lastModifiedByGuid + '\'' +
				", lastModifiedByName='" + lastModifiedByName + '\'' +
				", lastModifiedOn=" + lastModifiedOn +
				", comments=" + comments +
				", attachmentInfos=" + attachmentInfos +
				", agenda='" + agenda + '\'' +
				", status=" + status +
				", feedBackList=" + feedBackList +
				", feedbackClosed=" + feedbackClosed +
				", duration=" + duration +
				", location='" + location + '\'' +
				", likesCount=" + likesCount +
				", whatsForTrainees='" + whatsForTrainees + '\'' +
				", whatsForOrg='" + whatsForOrg + '\'' +
				'}';
	}

	public void setLowername() {
		this.lowername = StringUtils.lowerCase(name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Training)) return false;

		Training training = (Training) o;

		if (feedbackClosed != training.feedbackClosed) return false;
		if (id != null ? !id.equals(training.id) : training.id != null) return false;
		if (name != null ? !name.equals(training.name) : training.name != null) return false;
		if (likedBy != null ? !likedBy.equals(training.likedBy) : training.likedBy != null) return false;
		if (desc != null ? !desc.equals(training.desc) : training.desc != null) return false;
		if (score != null ? !score.equals(training.score) : training.score != null) return false;
		if (trainers != null ? !trainers.equals(training.trainers) : training.trainers != null) return false;
		if (scheduledOn != null ? !scheduledOn.equals(training.scheduledOn) : training.scheduledOn != null)
			return false;
		if (createDateTime != null ? !createDateTime.equals(training.createDateTime) : training.createDateTime != null)
			return false;
		if (trainees != null ? !trainees.equals(training.trainees) : training.trainees != null) return false;
		if (topics != null ? !topics.equals(training.topics) : training.topics != null) return false;
		if (prerequisites != null ? !prerequisites.equals(training.prerequisites) : training.prerequisites != null)
			return false;
		if (createdByGuid != null ? !createdByGuid.equals(training.createdByGuid) : training.createdByGuid != null)
			return false;
		if (createdByName != null ? !createdByName.equals(training.createdByName) : training.createdByName != null)
			return false;
		if (lastModifiedByGuid != null ? !lastModifiedByGuid.equals(training.lastModifiedByGuid) : training.lastModifiedByGuid != null)
			return false;
		if (lastModifiedByName != null ? !lastModifiedByName.equals(training.lastModifiedByName) : training.lastModifiedByName != null)
			return false;
		if (lastModifiedOn != null ? !lastModifiedOn.equals(training.lastModifiedOn) : training.lastModifiedOn != null)
			return false;
		if (comments != null ? !comments.equals(training.comments) : training.comments != null) return false;
		if (attachmentInfos != null ? !attachmentInfos.equals(training.attachmentInfos) : training.attachmentInfos != null)
			return false;
		if (agenda != null ? !agenda.equals(training.agenda) : training.agenda != null) return false;
		if (status != training.status) return false;
		if (feedBackList != null ? !feedBackList.equals(training.feedBackList) : training.feedBackList != null)
			return false;
		if (duration != null ? !duration.equals(training.duration) : training.duration != null) return false;
		if (location != null ? !location.equals(training.location) : training.location != null) return false;
		if (likesCount != null ? !likesCount.equals(training.likesCount) : training.likesCount != null) return false;
		if (whatsForTrainees != null ? !whatsForTrainees.equals(training.whatsForTrainees) : training.whatsForTrainees != null)
			return false;
		return whatsForOrg != null ? whatsForOrg.equals(training.whatsForOrg) : training.whatsForOrg == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (likedBy != null ? likedBy.hashCode() : 0);
		result = 31 * result + (desc != null ? desc.hashCode() : 0);
		result = 31 * result + (score != null ? score.hashCode() : 0);
		result = 31 * result + (trainers != null ? trainers.hashCode() : 0);
		result = 31 * result + (scheduledOn != null ? scheduledOn.hashCode() : 0);
		result = 31 * result + (createDateTime != null ? createDateTime.hashCode() : 0);
		result = 31 * result + (trainees != null ? trainees.hashCode() : 0);
		result = 31 * result + (topics != null ? topics.hashCode() : 0);
		result = 31 * result + (prerequisites != null ? prerequisites.hashCode() : 0);
		result = 31 * result + (createdByGuid != null ? createdByGuid.hashCode() : 0);
		result = 31 * result + (createdByName != null ? createdByName.hashCode() : 0);
		result = 31 * result + (lastModifiedByGuid != null ? lastModifiedByGuid.hashCode() : 0);
		result = 31 * result + (lastModifiedByName != null ? lastModifiedByName.hashCode() : 0);
		result = 31 * result + (lastModifiedOn != null ? lastModifiedOn.hashCode() : 0);
		result = 31 * result + (comments != null ? comments.hashCode() : 0);
		result = 31 * result + (attachmentInfos != null ? attachmentInfos.hashCode() : 0);
		result = 31 * result + (agenda != null ? agenda.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (feedBackList != null ? feedBackList.hashCode() : 0);
		result = 31 * result + (feedbackClosed ? 1 : 0);
		result = 31 * result + (duration != null ? duration.hashCode() : 0);
		result = 31 * result + (location != null ? location.hashCode() : 0);
		result = 31 * result + (likesCount != null ? likesCount.hashCode() : 0);
		result = 31 * result + (whatsForTrainees != null ? whatsForTrainees.hashCode() : 0);
		result = 31 * result + (whatsForOrg != null ? whatsForOrg.hashCode() : 0);
		return result;
	}

	public boolean isFeedbackClosed() {
		return feedbackClosed;
	}

	public void setFeedbackClosed(boolean feedbackClosed) {
		this.feedbackClosed = feedbackClosed;
	}

	public String getWhatsForTrainees() {
		return whatsForTrainees;
	}

	public void setWhatsForTrainees(String whatsForTrainees) {
		this.whatsForTrainees = whatsForTrainees;
	}

	public String getWhatsForOrg() {
		return whatsForOrg;
	}

	public void setWhatsForOrg(String whatsForOrg) {
		this.whatsForOrg = whatsForOrg;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getLastModifiedByName() {
		return lastModifiedByName;
	}

	public void setLastModifiedByName(String lastModifiedByName) {
		this.lastModifiedByName = lastModifiedByName;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Map<Long, String> getPrerequisites() {

		return prerequisites;
	}

	public void setPrerequisites(Map<Long, String> prerequisites) {
		this.prerequisites = prerequisites;
	}

	public List<FileAttachmentInfo> getAttachmentInfos() {
		return attachmentInfos;
	}

	public void setAttachmentInfos(List<FileAttachmentInfo> attachmentInfos) {
		this.attachmentInfos = attachmentInfos;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Training() {
	}

	public Training(Long id, TrainingStatus status) {
		this.id = id;
		this.status = status;
	}

	public String getLastModifiedByGuid() {
		return lastModifiedByGuid;
	}

	public void setLastModifiedByGuid(String lastModifiedByGuid) {
		this.lastModifiedByGuid = lastModifiedByGuid;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public String getCreatedByGuid() {
		return createdByGuid;
	}

	public void setCreatedByGuid(String createdByGuid) {
		this.createdByGuid = createdByGuid;
	}

	public Map<String, String> getLikedBy() {

		return likedBy;
	}

	public void setLikedBy(Map<String, String> likedBy) {
		this.likedBy = likedBy;
	}

	public Integer getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(Integer likesCount) {
		this.likesCount = likesCount;
	}

	public Map<Long, String> getTopics() {
		return topics;
	}

	public void setTopics(Map<Long, String> topics) {
		this.topics = topics;
	}

	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		lowername = StringUtils.lowerCase(name);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public TrainingStatus getStatus() {
		return status;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	public List<Long> getFeedBackList() {
		return feedBackList;
	}

	public void setFeedBackList(List<Long> feedBackList) {
		this.feedBackList = feedBackList;
	}

	public void setStatus(TrainingStatus status) {
		this.status = status;
	}

	public Map<String, String> getTrainers() {
		return trainers;
	}

	public void setTrainers(Map<String, String> trainers) {
		this.trainers = trainers;
	}

	public Date getScheduledOn() {
		return scheduledOn;
	}

	public void setScheduledOn(Date scheduledOn) {
		this.scheduledOn = scheduledOn;
	}

	public Map<String, String> getTrainees() {
		return trainees;
	}

	public void setTrainees(Map<String, String> trainees) {
		this.trainees = trainees;
	}
}