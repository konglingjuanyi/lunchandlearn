package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.domain.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by DE007RA on 8/8/2016.
 */
public interface EmployeeService {
	Page<Employee> getAll(Pageable pageable);

	Page<Employee> search(String term, Pageable pageable);

	List<Employee> getAllNames();

	Long getCount();

	Employee getEmployee(String empId);

	void deleteEmployee(String empId);

	Employee add(Employee employee);

	Employee update(Employee employee);

	List<Employee> getEmployeesByTopicKnown(String topicName);

	Map<Long, String> getTopicsKnown(String empGuid);

	Map<Long, String> getTopicsInterestedIn(String empGuid);

	List<MiniTrainingDetail> getTrainingsInterestedIn(String empGuid);

	List<MiniTrainingDetail> getTrainingsAttended(String empGuid);

	List<MiniTrainingDetail> getTrainingsImparted(String empGuid);

	boolean updateField(String empGuid, SimpleFieldEntry simpleFieldEntry) throws ParseException;

	void addTrainingTo(Map<String, String> employees, Training training, String trainingStr);

	void removeTrainingFrom(Map<String, String> employees, Long trainingId, String trainingStr);

	JSONObject getEmployeesMinimal();

	List<Employee> getEmployeesWithNotNullEmailId();

	void updateTopics(Long topicId, String topicNewName);

	void updateTrainings(Long trainingId, String trainingNewName);

	boolean updateTopicInterestedIn(String guid, Long topicId, String topicName);

	User getUser(String guid);

	List<Employee> findByRoles(List<String> roles);

	JSONArray getAllManagers();

	void updatedLdapEmployees() throws Exception;

	void updatedLdapEmployee(String guid);

	Long countByManagers(String guid);

	List<Employee> findAll();
}
