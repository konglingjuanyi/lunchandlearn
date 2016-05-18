package com.pb.lunchandlearn.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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
@Document(collection = "mongodb.collection.name.training")
public class Training {
	@Id
	private Long id;
	@NotNull
	@Min(3)
	private String name;
	@Max(500)
	private String comment;

	@NotNull
	private Map<String, String> trainers;//empId, name

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date scheduledOn;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date createDateTime;

	private Map<String, String> trainees;//empId, name
	@NotNull
	private TrainingStatus trainingStatus;
	private List<FeedBack> feedBackList;
	@NotNull
	private float duration;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Training)) return false;

		Training training = (Training) o;

		if (Float.compare(training.duration, duration) != 0) return false;
		if (!id.equals(training.id)) return false;
		if (!name.equals(training.name)) return false;
		if (comment != null ? !comment.equals(training.comment) : training.comment != null) return false;
		if (!trainers.equals(training.trainers)) return false;
		if (!scheduledOn.equals(training.scheduledOn)) return false;
		if (!createDateTime.equals(training.createDateTime)) return false;
		if (trainees != null ? !trainees.equals(training.trainees) : training.trainees != null) return false;
		if (trainingStatus != training.trainingStatus) return false;
		return feedBackList != null ? feedBackList.equals(training.feedBackList) : training.feedBackList == null;
	}

	@Override
	public String toString() {
		return "Training{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", comment='" + comment + '\'' +
				", trainers=" + trainers +
				", scheduledOn=" + scheduledOn +
				", createDateTime=" + createDateTime +
				", trainees=" + trainees +
				", trainingStatus=" + trainingStatus +
				", feedBackList=" + feedBackList +
				", duration=" + duration +
				'}';
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + (comment != null ? comment.hashCode() : 0);
		result = 31 * result + trainers.hashCode();
		result = 31 * result + scheduledOn.hashCode();
		result = 31 * result + createDateTime.hashCode();
		result = 31 * result + (trainees != null ? trainees.hashCode() : 0);
		result = 31 * result + (trainingStatus != null ? trainingStatus.hashCode() : 0);
		result = 31 * result + (feedBackList != null ? feedBackList.hashCode() : 0);
		result = 31 * result + (duration != +0.0f ? Float.floatToIntBits(duration) : 0);
		return result;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public TrainingStatus getTrainingStatus() {
		return trainingStatus;
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

	public void setTrainingStatus(TrainingStatus trainingStatus) {
		this.trainingStatus = trainingStatus;
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