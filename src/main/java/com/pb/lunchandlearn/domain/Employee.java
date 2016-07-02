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
@Document(collection = "employees")
public final class Employee {

	@Id
	@NotNull
	@Size(min = 4, max = 10)
	@Indexed
	@TextIndexed
	private String guid;

	@Size(min = 4, max = 20)
	@NotNull
	@Indexed
	@TextIndexed
	private String name;

	@NotNull
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
	private List<MiniTrainingDetail> trainingsInterestedIn;

	@Indexed
	@TextIndexed
	private List<MiniTrainingDetail> trainingsTaken;

	@Indexed
	@TextIndexed
	private Map<Long, String> topicsKnown;

	@Indexed
	@TextIndexed
	private Map<Long, String> topicsInterestedIn;

	@TextScore
	private Float score;

	public Employee() {}

	@Override
	public String toString() {
		return "Employee{" +
				"guid='" + guid + '\'' +
				", name='" + name + '\'' +
				", emailId='" + emailId + '\'' +
				", managers=" + managers +
				", trainingsInterestedIn=" + trainingsInterestedIn +
				", trainingsTaken=" + trainingsTaken +
				", topicsKnown=" + topicsKnown +
				", topicsInterestedIn=" + topicsInterestedIn +
				", score=" + score +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Employee)) return false;

		Employee employee = (Employee) o;

		if (guid != null ? !guid.equals(employee.guid) : employee.guid != null) return false;
		if (name != null ? !name.equals(employee.name) : employee.name != null) return false;
		if (emailId != null ? !emailId.equals(employee.emailId) : employee.emailId != null) return false;
		if (managers != null ? !managers.equals(employee.managers) : employee.managers != null) return false;
		if (trainingsInterestedIn != null ? !trainingsInterestedIn.equals(employee.trainingsInterestedIn) : employee.trainingsInterestedIn != null)
			return false;
		if (trainingsTaken != null ? !trainingsTaken.equals(employee.trainingsTaken) : employee.trainingsTaken != null)
			return false;
		if (topicsKnown != null ? !topicsKnown.equals(employee.topicsKnown) : employee.topicsKnown != null)
			return false;
		if (topicsInterestedIn != null ? !topicsInterestedIn.equals(employee.topicsInterestedIn) : employee.topicsInterestedIn != null)
			return false;
		return score != null ? score.equals(employee.score) : employee.score == null;

	}

	@Override
	public int hashCode() {
		int result = guid != null ? guid.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (emailId != null ? emailId.hashCode() : 0);
		result = 31 * result + (managers != null ? managers.hashCode() : 0);
		result = 31 * result + (trainingsInterestedIn != null ? trainingsInterestedIn.hashCode() : 0);
		result = 31 * result + (trainingsTaken != null ? trainingsTaken.hashCode() : 0);
		result = 31 * result + (topicsKnown != null ? topicsKnown.hashCode() : 0);
		result = 31 * result + (topicsInterestedIn != null ? topicsInterestedIn.hashCode() : 0);
		result = 31 * result + (score != null ? score.hashCode() : 0);
		return result;
	}

	public List<MiniTrainingDetail> getTrainingsInterestedIn() {
		return trainingsInterestedIn;
	}

	public void setTrainingsInterestedIn(List<MiniTrainingDetail> trainingsInterestedIn) {
		this.trainingsInterestedIn = trainingsInterestedIn;
	}

	public Map<Long, String> getTopicsInterestedIn() {
		return topicsInterestedIn;
	}

	public void setTopicsInterestedIn(Map<Long, String> topicsInterestedIn) {
		this.topicsInterestedIn = topicsInterestedIn;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Employee(String guid, String name, String emailId, Map<String, String> managers, List<MiniTrainingDetail> trainingsTaken, Map<Long, String> topicsKnown) {
		this.guid = guid;
		this.name = name;
		this.emailId = emailId;
		this.managers = managers;
		this.trainingsTaken = trainingsTaken;
		this.topicsKnown = topicsKnown;
	}

	public List<MiniTrainingDetail> getTrainingsTaken() {
		return trainingsTaken;
	}

	public void setTrainingsTaken(List<MiniTrainingDetail> trainingsTaken) {
		this.trainingsTaken = trainingsTaken;
	}

	public Map<Long, String> getTopicsKnown() {
		return topicsKnown;
	}

	public void setTopicsKnown(Map<Long, String> topicsKnown) {
		this.topicsKnown = topicsKnown;
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
