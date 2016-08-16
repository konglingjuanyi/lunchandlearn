package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.domain.Employee;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by DE007RA on 8/9/2016.
 */
public interface LDAPService {
	Map<String, String> getUsersDn();

	Employee addEmployee(String guid);

	void updateEmployees() throws Exception;

	void updateEmployee(String guid);

	Employee getEmployee(String guid);

	void authenticateEmployee(String guid, String pwd);
}
