package com.pb.lunchandlearn.domain;

import org.apache.commons.lang3.StringUtils;
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
@Document(collection = "trainingRooms")
public final class TrainingRoom {
	@Id
	private Long id;
	@NotNull()
	@Size(min = 2, max = 20)
	private String name;

	@TextIndexed
	@Indexed
	private String lowername;

	@TextIndexed
	@Indexed
	private String location;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "TrainingRoom{" +
				"id=" + id +
				", name='" + name + '\'' +
				", lowername='" + lowername + '\'' +
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
		if (!(o instanceof TrainingRoom)) return false;

		TrainingRoom that = (TrainingRoom) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (lowername != null ? !lowername.equals(that.lowername) : that.lowername != null) return false;
		if (createdByGuid != null ? !createdByGuid.equals(that.createdByGuid) : that.createdByGuid != null)
			return false;
		if (createdByName != null ? !createdByName.equals(that.createdByName) : that.createdByName != null)
			return false;
		if (lastModifiedByGuid != null ? !lastModifiedByGuid.equals(that.lastModifiedByGuid) : that.lastModifiedByGuid != null)
			return false;
		if (lastModifiedByName != null ? !lastModifiedByName.equals(that.lastModifiedByName) : that.lastModifiedByName != null)
			return false;
		if (createDateTime != null ? !createDateTime.equals(that.createDateTime) : that.createDateTime != null)
			return false;
		return lastModifiedOn != null ? lastModifiedOn.equals(that.lastModifiedOn) : that.lastModifiedOn == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (lowername != null ? lowername.hashCode() : 0);
		result = 31 * result + (createdByGuid != null ? createdByGuid.hashCode() : 0);
		result = 31 * result + (createdByName != null ? createdByName.hashCode() : 0);
		result = 31 * result + (lastModifiedByGuid != null ? lastModifiedByGuid.hashCode() : 0);
		result = 31 * result + (lastModifiedByName != null ? lastModifiedByName.hashCode() : 0);
		result = 31 * result + (createDateTime != null ? createDateTime.hashCode() : 0);
		result = 31 * result + (lastModifiedOn != null ? lastModifiedOn.hashCode() : 0);
		return result;
	}

	public String getLowername() {
		return lowername;
	}

	public void setLowername(String lowername) {
		this.lowername = lowername;
	}

	public void setLowername() {
		this.lowername = StringUtils.lowerCase(name);
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

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
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

	public TrainingRoom() {
		createDateTime = new Date();
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
		lowername = StringUtils.lowerCase(name);
	}
}
