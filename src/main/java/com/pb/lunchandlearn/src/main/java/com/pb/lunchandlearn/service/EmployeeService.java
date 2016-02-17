package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.model.Trainer;

public interface EmployeeService {
	Trainer create(Trainer trainer);
	Trainer findById(Long id);
	void delete(Long id);
	Iterable<Trainer> list();
	Trainer update(Trainer topic);
}