package com.pb.lunchandlearn.domain;

/**
 * Created by DE007RA on 6/20/2016.
 */
public final class SimpleFieldEntry {
	private String type;
	private String name;
	private Object value;

	public SimpleFieldEntry(String type, String name, Object value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public SimpleFieldEntry() {

	}

	@Override
	public String toString() {
		return "SimpleFieldEntry{" +
				"type='" + type + '\'' +
				", name='" + name + '\'' +
				", value='" + value + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SimpleFieldEntry)) return false;

		SimpleFieldEntry that = (SimpleFieldEntry) o;

		if (type != null ? !type.equals(that.type) : that.type != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		return value != null ? value.equals(that.value) : that.value == null;

	}

	@Override
	public int hashCode() {
		int result = type != null ? type.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		if(value != null) {
			return value;
		}
		return null;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
