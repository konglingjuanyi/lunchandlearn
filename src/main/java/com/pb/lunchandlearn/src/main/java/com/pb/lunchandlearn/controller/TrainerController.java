package com.pb.lunchandlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pb.lunchandlearn.model.Trainer;
import com.pb.lunchandlearn.service.EmployeeService;

/**
 * Created by DE007RA on 7/28/2015.
 */
@RestController("trainerController")
@RequestMapping("trainers")
public class TrainerController {

	@Autowired
	EmployeeService trainerService;

	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean add(@RequestBody Trainer trainer) {
		System.out.println("Trainer To add: " + trainer.toString());
		Boolean status = trainerService.create(trainer) != null;
		return status;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean update(@RequestBody Trainer trainer) {
		System.out.println("Trainer To update: " + trainer.toString());
		Boolean status = trainerService.update(trainer) != null;
		return status;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Trainer get(@PathVariable(value = "id") Long id) {
		Trainer trainer = trainerService.findById(id);
		return trainer;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<Trainer> list() {
		return trainerService.list();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean remove(@PathVariable("id") Long id) {
		Boolean status = Boolean.TRUE;
		System.out.println("Trainer To Del: " + id.toString());
		trainerService.delete(id);
		return status;
	}
}