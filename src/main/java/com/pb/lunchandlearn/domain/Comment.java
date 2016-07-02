package com.pb.lunchandlearn.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by DE007RA on 6/25/2016.
 */
@Document
public final class Comment {
	@Id
	@NotNull
	private Long id;
	private String ownerGuid;
	private String ownerName;
	private List<Comment> replies;
	private String text;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date dateTime;

	public Comment() {
		this.dateTime = new Date();
	}

	public Comment(String text) {
		this.dateTime = new Date();
		this.text = text;
	}

	@Override
	public String toString() {
		return "Comment{" +
				"id=" + id +
				", ownerGuid='" + ownerGuid + '\'' +
				", ownerName='" + ownerName + '\'' +
				", replies=" + replies +
				", text='" + text + '\'' +
				", dateTime=" + dateTime +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Comment)) return false;

		Comment comment = (Comment) o;

		if (id != null ? !id.equals(comment.id) : comment.id != null) return false;
		if (ownerGuid != null ? !ownerGuid.equals(comment.ownerGuid) : comment.ownerGuid != null) return false;
		if (ownerName != null ? !ownerName.equals(comment.ownerName) : comment.ownerName != null) return false;
		if (replies != null ? !replies.equals(comment.replies) : comment.replies != null) return false;
		if (text != null ? !text.equals(comment.text) : comment.text != null) return false;
		return dateTime != null ? dateTime.equals(comment.dateTime) : comment.dateTime == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (ownerGuid != null ? ownerGuid.hashCode() : 0);
		result = 31 * result + (ownerName != null ? ownerName.hashCode() : 0);
		result = 31 * result + (replies != null ? replies.hashCode() : 0);
		result = 31 * result + (text != null ? text.hashCode() : 0);
		result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
		return result;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerGuid() {
		return ownerGuid;
	}

	public void setOwnerGuid(String ownerGuid) {
		this.ownerGuid = ownerGuid;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public List<Comment> getReplies() {
		return replies;
	}

	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}
}
