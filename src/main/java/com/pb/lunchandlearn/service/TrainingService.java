package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by de007ra on 5/1/2016.
 */
@Service
public class TrainingService {
	@Autowired
	TrainingRepository trainingRepository;

	public List<Training> getAll() {
		return trainingRepository.findAll();
	}

	public Long getCount() {
		return trainingRepository.count();
	}

	public Training getTraining(String empId) {
		return trainingRepository.findOne(empId);
	}

	public Training getTrainingByName(String name) {
		return trainingRepository.findByName(name);
	}

	public void deleteTraining(String empId) {
		trainingRepository.delete(empId);
	}

	public Training add(Training training) {
		return trainingRepository.insert(training);
	}

	public Training editTraining(Training training) {
		return trainingRepository.save(training);
	}
}
