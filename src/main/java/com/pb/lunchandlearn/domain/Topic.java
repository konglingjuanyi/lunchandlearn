package com.pb.lunchandlearn.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by de007ra on 4/28/2016.
 */
@Document(collection = "topics")
public final class Topic {
	@Id
	private Long id;
	@NotNull()
	@Size(min = 2, max = 20)
	@TextIndexed
	private String name;
	@TextIndexed
	private String desc;

	private Map<String, String> interestedEmployees;//guid, name
	private Map<String, String> employeesKnowAbout;//guid, name
	private List<MiniTrainingDetail> trainings;
	private Integer likesCount;

	@NotNull
	private String createdBy;
	private String lastModifiedBy;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date createDateTime;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date lastModifiedOn;

	@Override
	public String toString() {
		return "Topic{" +
				"id=" + id +
				", name='" + name + '\'' +
				", desc='" + desc + '\'' +
				", interestedEmployees=" + interestedEmployees +
				", employeesKnowAbout=" + employeesKnowAbout +
				", trainings=" + trainings +
				", likesCount=" + likesCount +
				", createdBy='" + createdBy + '\'' +
				", lastModifiedBy='" + lastModifiedBy + '\'' +
				", createDateTime=" + createDateTime +
				", lastModifiedOn=" + lastModifiedOn +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Topic)) return false;

		Topic topicNew = (Topic) o;

		if (id != null ? !id.equals(topicNew.id) : topicNew.id != null) return false;
		if (name != null ? !name.equals(topicNew.name) : topicNew.name != null) return false;
		if (desc != null ? !desc.equals(topicNew.desc) : topicNew.desc != null) return false;
		if (interestedEmployees != null ? !interestedEmployees.equals(topicNew.interestedEmployees) : topicNew.interestedEmployees != null)
			return false;
		if (employeesKnowAbout != null ? !employeesKnowAbout.equals(topicNew.employeesKnowAbout) : topicNew.employeesKnowAbout != null)
			return false;
		if (trainings != null ? !trainings.equals(topicNew.trainings) : topicNew.trainings != null) return false;
		if (likesCount != null ? !likesCount.equals(topicNew.likesCount) : topicNew.likesCount != null) return false;
		if (createdBy != null ? !createdBy.equals(topicNew.createdBy) : topicNew.createdBy != null) return false;
		if (lastModifiedBy != null ? !lastModifiedBy.equals(topicNew.lastModifiedBy) : topicNew.lastModifiedBy != null)
			return false;
		if (createDateTime != null ? !createDateTime.equals(topicNew.createDateTime) : topicNew.createDateTime != null)
			return false;
		return lastModifiedOn != null ? lastModifiedOn.equals(topicNew.lastModifiedOn) : topicNew.lastModifiedOn == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (desc != null ? desc.hashCode() : 0);
		result = 31 * result + (interestedEmployees != null ? interestedEmployees.hashCode() : 0);
		result = 31 * result + (employeesKnowAbout != null ? employeesKnowAbout.hashCode() : 0);
		result = 31 * result + (trainings != null ? trainings.hashCode() : 0);
		result = 31 * result + (likesCount != null ? likesCount.hashCode() : 0);
		result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
		result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
		result = 31 * result + (createDateTime != null ? createDateTime.hashCode() : 0);
		result = 31 * result + (lastModifiedOn != null ? lastModifiedOn.hashCode() : 0);
		return result;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	public List<MiniTrainingDetail> getTrainings() {

		return trainings;
	}

	public void setTrainings(List<MiniTrainingDetail> trainings) {
		this.trainings = trainings;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public Map<String, String> getInterestedEmployees() {
		return interestedEmployees;
	}

	public void setInterestedEmployees(Map<String, String> interestedEmployees) {
		this.interestedEmployees = interestedEmployees;
	}

	public Map<String, String> getEmployeesKnowAbout() {
		return employeesKnowAbout;
	}

	public void setEmployeesKnowAbout(Map<String, String> employeesKnowAbout) {
		this.employeesKnowAbout = employeesKnowAbout;
	}

	public Integer getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(Integer likesCount) {
		this.likesCount = likesCount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Topic() {
		createDateTime = new Date();
		likesCount = 0;
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
}
