package com.pb.lunchandlearn.domain;

import com.pb.lunchandlearn.utils.CommonUtil;
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
	@Indexed
	@TextIndexed
	private String name;
	private Map<String, String> likedBy;//empId, name

	@Max(500)
	@Indexed
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
	private String createdBy;
	private String lastModifiedBy;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date lastModifiedOn;

	private List<Comment> comments;

	private List<FileAttachmentInfo> attachmentInfos;

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
				", createdBy='" + createdBy + '\'' +
				", lastModifiedBy='" + lastModifiedBy + '\'' +
				", lastModifiedOn=" + lastModifiedOn +
				", comments=" + comments +
				", attachmentInfos=" + attachmentInfos +
				", status=" + status +
				", feedBackList=" + feedBackList +
				", duration=" + duration +
				", likesCount=" + likesCount +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Training)) return false;

		Training training = (Training) o;

		if (Float.compare(training.duration, duration) != 0) return false;
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
		if (createdBy != null ? !createdBy.equals(training.createdBy) : training.createdBy != null) return false;
		if (lastModifiedBy != null ? !lastModifiedBy.equals(training.lastModifiedBy) : training.lastModifiedBy != null)
			return false;
		if (lastModifiedOn != null ? !lastModifiedOn.equals(training.lastModifiedOn) : training.lastModifiedOn != null)
			return false;
		if (comments != null ? !comments.equals(training.comments) : training.comments != null) return false;
		if (attachmentInfos != null ? !attachmentInfos.equals(training.attachmentInfos) : training.attachmentInfos != null)
			return false;
		if (status != training.status) return false;
		if (feedBackList != null ? !feedBackList.equals(training.feedBackList) : training.feedBackList != null)
			return false;
		return likesCount != null ? likesCount.equals(training.likesCount) : training.likesCount == null;

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
		result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
		result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
		result = 31 * result + (lastModifiedOn != null ? lastModifiedOn.hashCode() : 0);
		result = 31 * result + (comments != null ? comments.hashCode() : 0);
		result = 31 * result + (attachmentInfos != null ? attachmentInfos.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (feedBackList != null ? feedBackList.hashCode() : 0);
		result = 31 * result + (duration != +0.0f ? Float.floatToIntBits(duration) : 0);
		result = 31 * result + (likesCount != null ? likesCount.hashCode() : 0);
		return result;
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
		createDateTime = new Date();
		likesCount = 0;
		comments = CommonUtil.EMPTY_COMMENT_LIST;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Map<String, String> getLikedBy() {

		return likedBy;
	}

	public void setLikedBy(Map<String, String> likedBy) {
		this.likedBy = likedBy;
	}

	@NotNull
	private TrainingStatus status;
	private List<FeedBack> feedBackList;
	@NotNull
	private float duration;

	private Integer likesCount;

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

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
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

	public List<FeedBack> getFeedBackList() {
		return feedBackList;
	}

	public void setFeedBackList(List<FeedBack> feedBackList) {
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

	public class FeedBack {
		@Id
		private Long id;
		@NotNull
		private Map<String, String> trainee;
		private Map<String, Integer> ratings;//RatingName with point
		private String comment;

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof FeedBack)) return false;

			FeedBack feedBack = (FeedBack) o;

			if (!id.equals(feedBack.id)) return false;
			if (!trainee.equals(feedBack.trainee)) return false;
			if (ratings != null ? !ratings.equals(feedBack.ratings) : feedBack.ratings != null) return false;
			if (comment != null ? !comment.equals(feedBack.comment) : feedBack.comment != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = id.hashCode();
			result = 31 * result + trainee.hashCode();
			result = 31 * result + (ratings != null ? ratings.hashCode() : 0);
			result = 31 * result + (comment != null ? comment.hashCode() : 0);
			return result;
		}

		public Long getId() {

			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Map<String, String> getTrainee() {
			return trainee;
		}

		public void setTrainee(Map<String, String> trainee) {
			this.trainee = trainee;
		}

		public Map<String, Integer> getRatings() {
			return ratings;
		}

		public void setRatings(Map<String, Integer> ratings) {
			this.ratings = ratings;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}
	}
}