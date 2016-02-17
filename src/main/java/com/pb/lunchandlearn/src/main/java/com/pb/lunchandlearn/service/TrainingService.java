package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.model.Employee;

public interface TrainingService {
	Employee create(Employee emp);
	Employee findByGuid(String guid);
	void deleteByGuid(String guid);
	Iterable<Employee> list();
}