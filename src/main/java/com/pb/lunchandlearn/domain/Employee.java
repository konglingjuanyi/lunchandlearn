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
public final class Employee extends User {

	@Id
	@NotNull
	@Size(min = 4, max = 10)
	@TextIndexed
	private String guid;

	@TextIndexed
	private Map<String, String> managers;//guid, name

	@TextIndexed
	private List<MiniTrainingDetail> trainingsInterestedIn;

	@TextIndexed
	private List<MiniTrainingDetail> trainingsAttended;

	@TextIndexed
	private List<MiniTrainingDetail> trainingsImparted;

	@TextIndexed
	private Map<Long, String> topicsKnown;

	@TextIndexed
	private Map<Long, String> topicsInterestedIn;

	@Indexed
	private String empId;

	@TextScore
	private Float score;

	@Override
	public String toString() {
		return "Employee{" +
				"guid='" + guid + '\'' +
				", managers=" + managers +
				", trainingsInterestedIn=" + trainingsInterestedIn +
				", trainingsAttended=" + trainingsAttended +
				", trainingsImparted=" + trainingsImparted +
				", topicsKnown=" + topicsKnown +
				", topicsInterestedIn=" + topicsInterestedIn +
				", empId='" + empId + '\'' +
				", score=" + score +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Employee)) return false;
		if (!super.equals(o)) return false;

		Employee employee = (Employee) o;

		if (guid != null ? !guid.equals(employee.guid) : employee.guid != null) return false;
		if (managers != null ? !managers.equals(employee.managers) : employee.managers != null) return false;
		if (trainingsInterestedIn != null ? !trainingsInterestedIn.equals(employee.trainingsInterestedIn) : employee.trainingsInterestedIn != null)
			return false;
		if (trainingsAttended != null ? !trainingsAttended.equals(employee.trainingsAttended) : employee.trainingsAttended != null)
			return false;
		if (trainingsImparted != null ? !trainingsImparted.equals(employee.trainingsImparted) : employee.trainingsImparted != null)
			return false;
		if (topicsKnown != null ? !topicsKnown.equals(employee.topicsKnown) : employee.topicsKnown != null)
			return false;
		if (topicsInterestedIn != null ? !topicsInterestedIn.equals(employee.topicsInterestedIn) : employee.topicsInterestedIn != null)
			return false;
		if (empId != null ? !empId.equals(employee.empId) : employee.empId != null) return false;
		return score != null ? score.equals(employee.score) : employee.score == null;

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (guid != null ? guid.hashCode() : 0);
		result = 31 * result + (managers != null ? managers.hashCode() : 0);
		result = 31 * result + (trainingsInterestedIn != null ? trainingsInterestedIn.hashCode() : 0);
		result = 31 * result + (trainingsAttended != null ? trainingsAttended.hashCode() : 0);
		result = 31 * result + (trainingsImparted != null ? trainingsImparted.hashCode() : 0);
		result = 31 * result + (topicsKnown != null ? topicsKnown.hashCode() : 0);
		result = 31 * result + (topicsInterestedIn != null ? topicsInterestedIn.hashCode() : 0);
		result = 31 * result + (empId != null ? empId.hashCode() : 0);
		result = 31 * result + (score != null ? score.hashCode() : 0);
		return result;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public Employee() {}

	public Employee(String guid, String name, String emailId, List<String> roles) {
		super(name, emailId, roles);
		this.guid = guid.toUpperCase();
	}

	public List<MiniTrainingDetail> getTrainingsImparted() {
		return trainingsImparted;
	}

	public void setTrainingsImparted(List<MiniTrainingDetail> trainingsImparted) {
		this.trainingsImparted = trainingsImparted;
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

	public List<MiniTrainingDetail> getTrainingsAttended() {
		return trainingsAttended;
	}

	public void setTrainingsAttended(List<MiniTrainingDetail> trainingsAttended) {
		this.trainingsAttended = trainingsAttended;
	}

	public Map<Long, String> getTopicsKnown() {
		return topicsKnown;
	}

	public void setTopicsKnown(Map<Long, String> topicsKnown) {
		this.topicsKnown = topicsKnown;
	}

	public String getGuid() {
		return guid.toUpperCase();
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Map<String, String> getManagers() {
		return managers;
	}

	public void setManagers(Map<String, String> managers) {
		this.managers = managers;
	}
}
