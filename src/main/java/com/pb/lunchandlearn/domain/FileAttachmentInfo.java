package com.pb.lunchandlearn.domain;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by DE007RA on 6/26/2016.
 */
@Document
public final class FileAttachmentInfo {
	@NotNull
	private String fileName;
	@NotNull
	private Long size;
	@NotNull
	private String contentType;
	@NotNull
	private String ownerName;
	@NotNull
	private String ownerGuid;
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date uploadedOn;

	@Transient
	private InputStream file;

	public InputStream getFile() {
		return file;
	}

	public void setFile(InputStream file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "FileAttachmentInfo{" +
				"fileName='" + fileName + '\'' +
				", ownerName='" + ownerName + '\'' +
				", ownerGuid='" + ownerGuid + '\'' +
				", contentType='" + contentType + '\'' +
				", size=" + size +
				", uploadedOn=" + uploadedOn +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FileAttachmentInfo)) return false;

		FileAttachmentInfo that = (FileAttachmentInfo) o;

		if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
		if (ownerName != null ? !ownerName.equals(that.ownerName) : that.ownerName != null) return false;
		if (ownerGuid != null ? !ownerGuid.equals(that.ownerGuid) : that.ownerGuid != null) return false;
		if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) return false;
		if (size != null ? !size.equals(that.size) : that.size != null) return false;
		return uploadedOn != null ? uploadedOn.equals(that.uploadedOn) : that.uploadedOn == null;

	}

	@Override
	public int hashCode() {
		int result = fileName != null ? fileName.hashCode() : 0;
		result = 31 * result + (ownerName != null ? ownerName.hashCode() : 0);
		result = 31 * result + (ownerGuid != null ? ownerGuid.hashCode() : 0);
		result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
		result = 31 * result + (size != null ? size.hashCode() : 0);
		result = 31 * result + (uploadedOn != null ? uploadedOn.hashCode() : 0);
		return result;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerGuid() {
		return ownerGuid;
	}

	public void setOwnerGuid(String ownerGuid) {
		this.ownerGuid = ownerGuid;
	}

	public FileAttachmentInfo(String fileName, String contentType, Long size) {
		this.fileName = fileName;
		this.contentType = contentType;
		this.size = size;
		uploadedOn = new Date();
	}

	public FileAttachmentInfo() {}

	public FileAttachmentInfo(String fileName, String contentType, Long size, InputStream file) {
		this.fileName = fileName;
		this.contentType = contentType;
		this.size = size;
		this.file = file;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Date getUploadedOn() {
		return uploadedOn;
	}

	public void setUploadedOn(Date uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
