package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.domain.TrainingRoom;
import com.pb.lunchandlearn.repository.TrainingRoomRepository;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Created by de007ra on 5/1/2016.
 */
@Service
@PreAuthorize("hasAnyRole('ADMIN')")
public class TrainingRoomServiceImpl implements TrainingRoomService {
	@Autowired
	private TrainingRoomRepository trainingRoomRepository;

	@Override
	public Long getCount() {
		return trainingRoomRepository.count();
	}

	@Override
	public TrainingRoom getTrainingRoomByName(String trainingRoomName) {
		return trainingRoomRepository.findByName(trainingRoomName);
	}

	@Override
	public JSONObject getTrainingRoomById(Long trainingRoomId) {
		return CommonUtil.getTrainingRoomJson(trainingRoomRepository.findById(trainingRoomId));
	}

	@Override
	public void deleteTrainingRoom(Long trainingRoomId) {
		trainingRoomRepository.delete(trainingRoomId);
	}

	@Override
	public TrainingRoom add(TrainingRoom trainingRoom) {
		return trainingRoomRepository.insert(trainingRoom);
	}

	@Override
	public TrainingRoom update(TrainingRoom trainingRoom) {
		return trainingRoomRepository.save(trainingRoom);
	}

	@Override
	public JSONObject search(String term, Pageable pageable) {
		TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(term);
		return getTrainingRoomsJSON(trainingRoomRepository.findAllBy(textCriteria, pageable), true);
	}

	@Override
	public JSONObject getAll(Pageable pageable) {
		return getTrainingRoomsJSON(trainingRoomRepository.findAll(pageable), false);
	}

	@Override
	public JSONArray getAllBrief() {
		return CommonUtil.getTrainingRoomsJson(trainingRoomRepository.findAll());
	}

	private static JSONObject getTrainingRoomsJSON(Page<TrainingRoom> trainingRooms, boolean contentOnly) {
		JSONObject jsonObject = new JSONObject();
		if (!contentOnly) {
			jsonObject = CommonUtil.setPaginationInfo(trainingRooms, jsonObject);
		}
		jsonObject.put("content", CommonUtil.getTrainingRoomsJson(trainingRooms.iterator()));
		return jsonObject;
	}

}