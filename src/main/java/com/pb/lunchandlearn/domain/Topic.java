package com.pb.lunchandlearn.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
	@Indexed
	private String name;
	@TextIndexed
	private String desc;

	private Map<String, String> interestedEmployees;//guid, name
	private Map<String, String> employeesKnowAbout;//guid, name
	private List<MiniTrainingDetail> trainings;
	private Integer likesCount;

	@NotNull
	private String createdByGuid;
	private String createdByName;
	private String lastModifiedByGuid;
	private String lastModifiedByName;

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
				", createdByGuid='" + createdByGuid + '\'' +
				", createdByName='" + createdByName + '\'' +
				", lastModifiedByGuid='" + lastModifiedByGuid + '\'' +
				", lastModifiedByName='" + lastModifiedByName + '\'' +
				", createDateTime=" + createDateTime +
				", lastModifiedOn=" + lastModifiedOn +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Topic)) return false;

		Topic topic = (Topic) o;

		if (id != null ? !id.equals(topic.id) : topic.id != null) return false;
		if (name != null ? !name.equals(topic.name) : topic.name != null) return false;
		if (desc != null ? !desc.equals(topic.desc) : topic.desc != null) return false;
		if (interestedEmployees != null ? !interestedEmployees.equals(topic.interestedEmployees) : topic.interestedEmployees != null)
			return false;
		if (employeesKnowAbout != null ? !employeesKnowAbout.equals(topic.employeesKnowAbout) : topic.employeesKnowAbout != null)
			return false;
		if (trainings != null ? !trainings.equals(topic.trainings) : topic.trainings != null) return false;
		if (likesCount != null ? !likesCount.equals(topic.likesCount) : topic.likesCount != null) return false;
		if (createdByGuid != null ? !createdByGuid.equals(topic.createdByGuid) : topic.createdByGuid != null)
			return false;
		if (createdByName != null ? !createdByName.equals(topic.createdByName) : topic.createdByName != null)
			return false;
		if (lastModifiedByGuid != null ? !lastModifiedByGuid.equals(topic.lastModifiedByGuid) : topic.lastModifiedByGuid != null)
			return false;
		if (lastModifiedByName != null ? !lastModifiedByName.equals(topic.lastModifiedByName) : topic.lastModifiedByName != null)
			return false;
		if (createDateTime != null ? !createDateTime.equals(topic.createDateTime) : topic.createDateTime != null)
			return false;
		return lastModifiedOn != null ? lastModifiedOn.equals(topic.lastModifiedOn) : topic.lastModifiedOn == null;

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
		result = 31 * result + (createdByGuid != null ? createdByGuid.hashCode() : 0);
		result = 31 * result + (createdByName != null ? createdByName.hashCode() : 0);
		result = 31 * result + (lastModifiedByGuid != null ? lastModifiedByGuid.hashCode() : 0);
		result = 31 * result + (lastModifiedByName != null ? lastModifiedByName.hashCode() : 0);
		result = 31 * result + (createDateTime != null ? createDateTime.hashCode() : 0);
		result = 31 * result + (lastModifiedOn != null ? lastModifiedOn.hashCode() : 0);
		return result;
	}

	public String getLastModifiedByName() {
		return lastModifiedByName;
	}

	public void setLastModifiedByName(String lastModifiedByName) {
		this.lastModifiedByName = lastModifiedByName;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
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

	public String getCreatedByGuid() {
		return createdByGuid;
	}

	public void setCreatedByGuid(String createdByGuid) {
		this.createdByGuid = createdByGuid;
	}

	public String getLastModifiedByGuid() {
		return lastModifiedByGuid;
	}

	public void setLastModifiedByGuid(String lastModifiedByGuid) {
		this.lastModifiedByGuid = lastModifiedByGuid;
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
