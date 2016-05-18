package com.pb.lunchandlearn.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;

/**
 * Created by de007ra on 4/28/2016.
 */
@Document(collection = "{mongodb.collection.name.topic.name}")
public class Topic {
	@Id
	@NotNull
	@NotEmpty
	private Long id;
	@NotNull()
	@Size(min = 2, max = 20)
	private String name;
	private Map<String, String> interestedEmployees;
	private Map<String, String> employeesKnowAbout;
	private Map<String, String> trainings;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date createDateTime;

	public Topic() {
		createDateTime = new Date();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInterestedEmployees(Map<String, String> interestedEmployees) {
		this.interestedEmployees = interestedEmployees;
	}

	public void setEmployeesKnowAbout(Map<String, String> employeesKnowAbout) {
		this.employeesKnowAbout = employeesKnowAbout;
	}

	public void setTrainings(Map<String, String> trainings) {
		this.trainings = trainings;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	@Override
	public String toString() {
		return "Topic{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", interestedEmployees=" + interestedEmployees +
				", employeesKnowAbout=" + employeesKnowAbout +
				", trainings=" + trainings +
				", createDateTime=" + createDateTime +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Topic)) return false;

		Topic topic = (Topic) o;

		if (!id.equals(topic.id)) return false;
		if (!name.equals(topic.name)) return false;
		if (interestedEmployees != null ? !interestedEmployees.equals(topic.interestedEmployees) : topic.interestedEmployees != null)
			return false;
		if (employeesKnowAbout != null ? !employeesKnowAbout.equals(topic.employeesKnowAbout) : topic.employeesKnowAbout != null)
			return false;
		if (trainings != null ? !trainings.equals(topic.trainings) : topic.trainings != null) return false;
		return createDateTime.equals(topic.createDateTime);

	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + (interestedEmployees != null ? interestedEmployees.hashCode() : 0);
		result = 31 * result + (employeesKnowAbout != null ? employeesKnowAbout.hashCode() : 0);
		result = 31 * result + (trainings != null ? trainings.hashCode() : 0);
		result = 31 * result + createDateTime.hashCode();
		return result;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Map<String, String> getInterestedEmployees() {
		return interestedEmployees;
	}

	public Map<String, String> getEmployeesKnowAbout() {
		return employeesKnowAbout;
	}

	public Map<String, String> getTrainings() {
		return trainings;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}
}
