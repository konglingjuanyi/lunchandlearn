package com.pb.lunchandlearn.web;

/**
 * Created by DE007RA on 4/27/2016.
 */
import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
	@Autowired
	public TrainingService trainingService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Training> home() {
		return trainingService.getAll();
	}

	@RequestMapping(value = "/top", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Training> topTrainings() {
		return trainingService.getAll();
	}

	@RequestMapping(value = "/recent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Training> recentTrainings() {
		return trainingService.getAll();
	}

	@RequestMapping(value="/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long getCount() {
		return trainingService.getCount();
	}

	@RequestMapping(value="/training/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Training getTraining(@PathVariable("id") String empId) {
		return trainingService.getTraining(empId);
	}

	@RequestMapping(value="/training", method = RequestMethod.POST)
	public Training addTraining(@Valid Training training, BindingResult result) {
		if(!result.hasErrors()) {
			return trainingService.add(training);
		}
		return null;
	}

	@RequestMapping(value="/training", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void editTraining(@Valid Training training) {
		trainingService.editTraining(training);
	}

	@RequestMapping(value="/training/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTraining(@PathVariable("id") String empId) {
		trainingService.deleteTraining(empId);
	}
}