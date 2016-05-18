package com.pb.lunchandlearn.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by de007ra on 4/28/2016.
 */
@Document(collection = "{mongodb.collection.name.rating}")
public class RatingName {
	@NotNull
	@Min(3)
	private String name;
	private String desc;

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (desc != null ? desc.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RatingName)) return false;

		RatingName that = (RatingName) o;

		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		return desc != null ? desc.equals(that.desc) : that.desc == null;

	}

	@Override
	public String toString() {
		return "RatingName{" +
				"name='" + name + '\'' +
				", desc='" + desc + '\'' +
				'}';
	}

	public String getDesc() {

		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
