package com.pb.lunchandlearn.domain;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * Created by de007ra on 4/28/2016.
 */
@Document(collection = "{mongodb.collection.name.employee}")
public class Employee {

	@Id
	@NotNull
	@Size(min = 4, max = 10)
	@Indexed
	@TextIndexed
	private String guid;

	@Size(min = 4, max = 20)
	@NotNull()
	@Indexed
	@TextIndexed
	private String name;

	@NotNull()
	@Email
	@Size(min = 7, max = 26)
	@Indexed
	@TextIndexed
	private String emailId;

	@Indexed
	@TextIndexed
	private Map<String, String> managers;//guid, name

	@Indexed
	@TextIndexed
	private Map<String, String> trainingsTaken;

	@Indexed
	@TextIndexed
	private Map<String, String> trainingsGiven;

	@Indexed
	@TextIndexed
	private List<String> knowTopics;

	@TextScore
	private Float score;

	public Employee() {
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Employee(String guid, String name, String emailId, Map<String, String> managers, Map<String, String> trainingsTaken, Map<String, String> trainingsGiven, List<String> knowTopics) {
		this.guid = guid;
		this.name = name;
		this.emailId = emailId;
		this.managers = managers;
		this.trainingsTaken = trainingsTaken;
		this.trainingsGiven = trainingsGiven;
		this.knowTopics = knowTopics;
	}

	public Map<String, String> getTrainingsTaken() {
		return trainingsTaken;
	}

	@Override
	public String toString() {
		return "Employee{" +
				"guid='" + guid + '\'' +
				", name='" + name + '\'' +
				", emailId='" + emailId + '\'' +
				", managers=" + managers +
				", trainingsTaken=" + trainingsTaken +
				", trainingsGiven=" + trainingsGiven +
				", knowTopics=" + knowTopics +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Employee)) return false;

		Employee employee = (Employee) o;

		if (!guid.equals(employee.guid)) return false;
		if (!name.equals(employee.name)) return false;
		if (!emailId.equals(employee.emailId)) return false;
		if (managers != null ? !managers.equals(employee.managers) : employee.managers != null) return false;
		if (trainingsTaken != null ? !trainingsTaken.equals(employee.trainingsTaken) : employee.trainingsTaken != null)
			return false;
		if (trainingsGiven != null ? !trainingsGiven.equals(employee.trainingsGiven) : employee.trainingsGiven != null)
			return false;
		return knowTopics != null ? knowTopics.equals(employee.knowTopics) : employee.knowTopics == null;

	}

	@Override
	public int hashCode() {
		int result = (guid != null ? guid.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (emailId != null ? emailId.hashCode() : 0);;
		result = 31 * result + (managers != null ? managers.hashCode() : 0);
		result = 31 * result + (trainingsTaken != null ? trainingsTaken.hashCode() : 0);
		result = 31 * result + (trainingsGiven != null ? trainingsGiven.hashCode() : 0);
		result = 31 * result + (knowTopics != null ? knowTopics.hashCode() : 0);
		return result;
	}

	public void setTrainingsTaken(Map<String, String> trainingsTaken) {
		this.trainingsTaken = trainingsTaken;
	}

	public Map<String, String> getTrainingsGiven() {
		return trainingsGiven;
	}

	public void setTrainingsGiven(Map<String, String> trainingsGiven) {
		this.trainingsGiven = trainingsGiven;
	}

	public List<String> getKnowTopics() {
		return knowTopics;
	}

	public void setKnowTopics(List<String> knowTopics) {
		this.knowTopics = knowTopics;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Map<String, String> getManagers() {
		return managers;
	}

	public void setManagers(Map<String, String> managers) {
		this.managers = managers;
	}
}
