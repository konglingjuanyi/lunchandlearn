package com.pb.lunchandlearn.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by de007ra on 7/8/2016.
 */
@Document(collection = "feedbacks")
public final class FeedBack {
	@Id
	private Long id;

	@Indexed
	private Long parentId;
	@NotNull
	private String respondentGuid;
	@NotNull
	private String respondentName;
	private Map<String, Integer> ratings;//RatingName with point
	private String comment;

	@Override
	public String toString() {
		return "FeedBack{" +
				"id=" + id +
				", parentId=" + parentId +
				", respondentGuid='" + respondentGuid + '\'' +
				", respondentName='" + respondentName + '\'' +
				", ratings=" + ratings +
				", comment='" + comment + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FeedBack)) return false;

		FeedBack feedBack = (FeedBack) o;

		if (id != null ? !id.equals(feedBack.id) : feedBack.id != null) return false;
		if (parentId != null ? !parentId.equals(feedBack.parentId) : feedBack.parentId != null) return false;
		if (respondentGuid != null ? !respondentGuid.equals(feedBack.respondentGuid) : feedBack.respondentGuid != null)
			return false;
		if (respondentName != null ? !respondentName.equals(feedBack.respondentName) : feedBack.respondentName != null)
			return false;
		if (ratings != null ? !ratings.equals(feedBack.ratings) : feedBack.ratings != null) return false;
		return comment != null ? comment.equals(feedBack.comment) : feedBack.comment == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
		result = 31 * result + (respondentGuid != null ? respondentGuid.hashCode() : 0);
		result = 31 * result + (respondentName != null ? respondentName.hashCode() : 0);
		result = 31 * result + (ratings != null ? ratings.hashCode() : 0);
		result = 31 * result + (comment != null ? comment.hashCode() : 0);
		return result;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getRespondentGuid() {
		return respondentGuid;
	}

	public void setRespondentGuid(String respondentGuid) {
		this.respondentGuid = respondentGuid;
	}

	public String getRespondentName() {
		return respondentName;
	}

	public void setRespondentName(String respondentName) {
		this.respondentName = respondentName;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Integer> getRatings() {
		return ratings;
	}

	public void setRatings(Map<String, Integer> ratings) {
		this.ratings = ratings;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}