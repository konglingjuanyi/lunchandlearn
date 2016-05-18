package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by de007ra on 5/1/2016.
 */
@Service
public class EmployeeService {
	@Autowired
	EmployeeRepository employeeRepository;
	public EmployeeService() {
	}

	public Page<Employee> getAll(Pageable pageable) {
		return employeeRepository.findAll(pageable);
/*
		List<Employee> list = new ArrayList<>();
		while(iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
*/
	}

	public Page<Employee> search(String term, Pageable pageable) {
		TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(term);
		return employeeRepository.findAllBy(textCriteria, pageable);
	}

	public List<Employee> getAllNames() {
		return employeeRepository.findAllByNameNotNull();
	}

	public Long getCount() {
		return employeeRepository.count();
	}

	public Employee getEmployee(String empId) {
		return employeeRepository.findOne(empId);
	}

	public Employee getEmployeeByName(String name) {
		return employeeRepository.findByName(name);
	}

	public void deleteEmployee(String empId) {
		employeeRepository.delete(empId);
	}

	public Employee add(Employee employee) {
		return employeeRepository.insert(employee);
	}

	public Employee editEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	public List<Employee> getEmployeesByTopic(String topicName) {
		return employeeRepository.findByKnowTopics(Arrays.asList(topicName));
	}

	public List<Employee> getEmployeesByTopics(List<String> topicsName) {
		return employeeRepository.findByKnowTopics(topicsName);
	}
}
