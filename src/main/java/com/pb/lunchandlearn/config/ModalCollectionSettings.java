package com.pb.lunchandlearn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by de007ra on 5/3/2016.
 */
@Component
@ConfigurationProperties(prefix = "mongodb.collection.name")
public class ModalCollectionSettings {
	private String employee;
	private String rating;
	private String training;
	private String collectionId;
	private String topic;

	@Override
	public String toString() {
		return "ModalCollectionSettings{" +
				"employee='" + employee + '\'' +
				", rating='" + rating + '\'' +
				", training='" + training + '\'' +
				", collectionId='" + collectionId + '\'' +
				", topic='" + topic + '\'' +
				'}';
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ModalCollectionSettings)) return false;

		ModalCollectionSettings that = (ModalCollectionSettings) o;

		if (!employee.equals(that.employee)) return false;
		if (!rating.equals(that.rating)) return false;
		if (!training.equals(that.training)) return false;
		if (!collectionId.equals(that.collectionId)) return false;
		return topic.equals(that.topic);

	}

	@Override
	public int hashCode() {
		int result = employee.hashCode();
		result = 31 * result + rating.hashCode();
		result = 31 * result + training.hashCode();
		result = 31 * result + collectionId.hashCode();
		result = 31 * result + topic.hashCode();
		return result;
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
