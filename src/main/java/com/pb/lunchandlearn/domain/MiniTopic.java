package com.pb.lunchandlearn.domain;

/**
 * Created by de007ra on 9/23/2016.
 */
public final class MiniTopic {
	private Long id;
	private String name;
	private String desc;

	public MiniTopic(Long id, String name, String desc) {
		this.id = id;
		this.name = name;
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "MiniTopic{" +
				"id=" + id +
				", name='" + name + '\'' +
				", desc='" + desc + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MiniTopic)) return false;

		MiniTopic miniTopic = (MiniTopic) o;

		if (id != null ? !id.equals(miniTopic.id) : miniTopic.id != null) return false;
		if (name != null ? !name.equals(miniTopic.name) : miniTopic.name != null) return false;
		return desc != null ? desc.equals(miniTopic.desc) : miniTopic.desc == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (desc != null ? desc.hashCode() : 0);
		return result;
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