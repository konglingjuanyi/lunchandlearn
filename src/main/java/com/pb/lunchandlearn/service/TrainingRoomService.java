package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.domain.TrainingRoom;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by DE007RA on 8/9/2016.
 */
public interface TrainingRoomService {
	Long getCount();

	TrainingRoom getTrainingRoomByName(String trainingRoomName);

	JSONObject getTrainingRoomById(Long trainingRoomId);

	void deleteTrainingRoom(Long trainingRoomId);

	TrainingRoom add(TrainingRoom trainingRoom);

	TrainingRoom update(TrainingRoom trainingRoom);

	JSONObject search(String term, Pageable pageable);

	JSONObject getAll(Pageable pageable);

	JSONArray getAllBrief();
}