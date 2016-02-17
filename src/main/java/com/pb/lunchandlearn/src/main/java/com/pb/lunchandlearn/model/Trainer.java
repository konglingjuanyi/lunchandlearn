package com.pb.lunchandlearn.model;

import java.util.List;

/**
 * Created by DE007RA on 7/28/2015.
 */
public class Trainer extends Employee {
	public List<String> getTrainingTopicIds() {
		return trainingTopicIds;
	}

	public void setTrainingTopicIds(List<String> trainingTopicIds) {
		this.trainingTopicIds = trainingTopicIds;
	}

	public List<String> getExpertiseFields() {
		return expertiseFields;
	}

	public Trainer(String guid, String name, String emailId,
			List<String> roles, String reportsTo,
			List<String> trainingTopicIds, List<String> expertiseFields) {
		super(guid, name, emailId, roles, reportsTo);
		this.trainingTopicIds = trainingTopicIds;
		this.expertiseFields = expertiseFields;
	}

	public Trainer(Long id, String name, String emailId, Gender gender) {
		super(id, name, emailId, gender);
	}

	public void setExpertiseFields(List<String> expertiseFields) {
		this.expertiseFields = expertiseFields;
	}

	private List<String> trainingTopicIds;
	private List<String> expertiseFields;
}