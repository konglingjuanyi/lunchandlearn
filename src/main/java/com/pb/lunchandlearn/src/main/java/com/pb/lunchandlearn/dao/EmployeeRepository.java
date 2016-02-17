package com.pb.lunchandlearn.dao;

import org.springframework.stereotype.Repository;

import com.pb.lunchandlearn.model.Employee;

public interface EmployeeRepository {

	Employee findByGuid(String guid);

//	Long deleteByGuid(String guid);

}
