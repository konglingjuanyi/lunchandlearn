package com.pb.lunchandlearn.domain;

import org.springframework.util.StringUtils;

/**
 * Created by de007ra on 5/2/2016.
 */
public enum TrainingStatus {
	NOMINATED,
	SCHEDULED,
	CANCELLED,
	COMPLETED,
	POSTPONED;
	private String name;
	private TrainingStatus() {
		this.name = StringUtils.capitalize(name().toLowerCase());
	}
	@Override
	public String toString() {
		return name;
	}
}
