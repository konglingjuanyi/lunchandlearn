package com.pb.lunchandlearn.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by de007ra on 5/3/2016.
 */
@Document(collection = "{mongodb.collection.name.collectionId}")
public class CollectionId {
	private String collectionName;
	private Long lastId;

	@Override
	public String toString() {
		return "CollectionId{" +
				"collectionName='" + collectionName + '\'' +
				", lastId=" + lastId +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CollectionId)) return false;

		CollectionId that = (CollectionId) o;

		if (!collectionName.equals(that.collectionName)) return false;
		return lastId.equals(that.lastId);

	}

	@Override
	public int hashCode() {
		int result = collectionName.hashCode();
		result = 31 * result + lastId.hashCode();
		return result;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public Long getLastId() {
		return lastId;
	}

	public void setLastId(Long lastId) {
		this.lastId = lastId;
	}
}
