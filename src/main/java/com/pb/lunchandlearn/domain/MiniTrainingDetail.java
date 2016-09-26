package com.pb.lunchandlearn.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by DE007RA on 6/20/2016.
 */
@Document
public final class MiniTrainingDetail {
	@Id
	private Long id;
	private String name;
	private TrainingStatus status;

	public String getName() {

		return name;
	}

	public MiniTrainingDetail() {}

	public MiniTrainingDetail(Long id, String name, TrainingStatus status) {
		this.name = name;
		this.status = status;
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TrainingStatus getStatus() {
		return status;
	}

	public void setScheduledOn(TrainingStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "MiniTrainingDetail{" +
				"id=" + id +
				", name='" + name + '\'' +
				", status=" + status +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MiniTrainingDetail)) return false;

		MiniTrainingDetail that = (MiniTrainingDetail) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		return status != null ? status.equals(that.status) : that.status == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		return result;
	}
}
