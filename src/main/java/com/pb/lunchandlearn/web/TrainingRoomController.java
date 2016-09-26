package com.pb.lunchandlearn.web;

/**
 * Created by DE007RA on 4/27/2016.
 */
import com.pb.lunchandlearn.config.LikeType;
import com.pb.lunchandlearn.domain.MiniTrainingDetail;
import com.pb.lunchandlearn.domain.SimpleFieldEntry;
import com.pb.lunchandlearn.domain.TrainingRoom;
import com.pb.lunchandlearn.domain.TrainingRoom;
import com.pb.lunchandlearn.service.TrainingRoomServiceImpl;
import com.pb.lunchandlearn.service.TrainingRoomService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainingrooms")
public class TrainingRoomController {
	@Autowired
	public TrainingRoomService trainingRoomService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject list(Pageable pageable, @RequestParam(value = "search", required = false) String searchTerm) {
		if(!StringUtils.isEmpty(searchTerm)) {
			return trainingRoomService.search(searchTerm, pageable);
		}
		return trainingRoomService.getAll(pageable);
	}

	@RequestMapping(value = "/brief", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray listAll() {
		return trainingRoomService.getAllBrief();
	}

	@RequestMapping(value="/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long getCount() {
		return trainingRoomService.getCount();
	}

	@RequestMapping(value="/trainingroom", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TrainingRoom addTrainingRoom(@RequestBody TrainingRoom trainingroom, BindingResult result) {
		if(!result.hasErrors()) {
			return trainingRoomService.add(trainingroom);
		}
		return null;
	}

	@RequestMapping(value="/trainingroom", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void updateTrainingRoom(@RequestBody TrainingRoom trainingroom) {
		trainingRoomService.update(trainingroom);
	}

	@RequestMapping(value="/trainingroom/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTrainingRoom(@PathVariable("id") Long trainingRoomId) {
		trainingRoomService.deleteTrainingRoom(trainingRoomId);
	}

	@RequestMapping(value="/trainingroom/{id}", method = RequestMethod.GET)
	public JSONObject getTrainingRoom(@PathVariable("id") Long trainingRoomId) {
		return trainingRoomService.getTrainingRoomById(trainingRoomId);
	}
}