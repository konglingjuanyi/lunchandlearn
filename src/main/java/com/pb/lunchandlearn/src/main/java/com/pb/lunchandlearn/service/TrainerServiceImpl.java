package com.pb.lunchandlearn.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.pb.lunchandlearn.model.Employee.Gender;
import com.pb.lunchandlearn.model.Trainer;

@Service("trainerService")
public class TrainerServiceImpl implements EmployeeService {

	List<Trainer> trainers = new ArrayList<Trainer>();
//	@Autowired
//	EmployeeRepository employeeRepository;

	@PostConstruct
	void initEmps() {
		trainers.add(new Trainer(1l, "Akhil Shrivastava", "akhil@pb.com", Gender.MALE));
		trainers.add(new Trainer(2l, "Vijaya Laxmi", "vijaya@pb.com", Gender.FEMALE));
		trainers.add(new Trainer(3l, "Divya Pathak", "divya@pb.com", Gender.FEMALE));
		trainers.add(new Trainer(4l, "Ravi", "ravi@pb.com", Gender.MALE));
		trainers.add(new Trainer(5l, "Rajneesh", "rajneesh@pb.com", Gender.MALE));
	}

	@Override
	public Trainer create(Trainer trainer) {
		trainers.add(trainer);
		Trainer savedTopic = trainers.get(trainers.size() - 1);
		return savedTopic;
	}

	@Override
	public Trainer findById(Long id) {
		for(Trainer trainer : trainers) {
			if(trainer.getId().equals(id)) {
				return trainer;
			}
		}
//		employeeRepository.findByGuid(guid);
		return null;
	}

	@Override
	public void delete(Long id) {
		Trainer trainerToDelete = null;
		for(Trainer trainer : trainers) {
			if(trainer.getId().equals(id)) {
				trainerToDelete = trainer;
				break;
			}
		}
		if(trainerToDelete != null) {
			trainers.remove(trainerToDelete);
		}
	}

	@Override
	public Iterable<Trainer> list() {
		return trainers;
	}

	@Override
	public Trainer update(Trainer trainer) {
		for(Trainer t : trainers) {
			if(t.getId().equals(trainer.getId())) {
				t.setName(trainer.getName());
				t.setEmailId(trainer.getEmailId());
				t.setGender(trainer.getGender());
				return t;
			}
		}
		return null;
	}}