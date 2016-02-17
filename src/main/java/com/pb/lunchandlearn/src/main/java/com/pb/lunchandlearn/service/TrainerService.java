package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.model.Employee;

public interface TrainerService {
	Employee create(Employee emp);
	Employee findByGuid(String guid);
	void deleteByGuid(String guid);
	Iterable<Employee> list();
}