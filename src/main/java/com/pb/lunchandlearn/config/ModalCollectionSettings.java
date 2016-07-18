package com.pb.lunchandlearn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by de007ra on 5/3/2016.
 */
@Component
@ConfigurationProperties(prefix = "mongodb.collection.name")
public final class ModalCollectionSettings {
	private String employee;
	private String rating;
	private String training;
	private String collectionId;
	private String topic;
	private String comment;
	private String feedback;

	@Override
	public String toString() {
		return "ModalCollectionSettings{" +
				"employee='" + employee + '\'' +
				", rating='" + rating + '\'' +
				", training='" + training + '\'' +
				", collectionId='" + collectionId + '\'' +
				", topic='" + topic + '\'' +
				", comment='" + comment + '\'' +
				", feedback='" + feedback + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ModalCollectionSettings)) return false;

		ModalCollectionSettings that = (ModalCollectionSettings) o;

		if (employee != null ? !employee.equals(that.employee) : that.employee != null) return false;
		if (rating != null ? !rating.equals(that.rating) : that.rating != null) return false;
		if (training != null ? !training.equals(that.training) : that.training != null) return false;
		if (collectionId != null ? !collectionId.equals(that.collectionId) : that.collectionId != null) return false;
		if (topic != null ? !topic.equals(that.topic) : that.topic != null) return false;
		if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
		return feedback != null ? feedback.equals(that.feedback) : that.feedback == null;

	}

	@Override
	public int hashCode() {
		int result = employee != null ? employee.hashCode() : 0;
		result = 31 * result + (rating != null ? rating.hashCode() : 0);
		result = 31 * result + (training != null ? training.hashCode() : 0);
		result = 31 * result + (collectionId != null ? collectionId.hashCode() : 0);
		result = 31 * result + (topic != null ? topic.hashCode() : 0);
		result = 31 * result + (comment != null ? comment.hashCode() : 0);
		result = 31 * result + (feedback != null ? feedback.hashCode() : 0);
		return result;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getTraining() {
		return training;
	}

	public void setTraining(String training) {
		this.training = training;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public String getTopic() {
		return topic;
	}
}
