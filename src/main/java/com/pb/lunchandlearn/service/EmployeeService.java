package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.domain.MiniTrainingDetail;
import com.pb.lunchandlearn.domain.SimpleFieldEntry;
import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.repository.EmployeeRepository;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

	public List<Employee> getEmployeesByTopicKnown(String topicName) {
		return employeeRepository.findByTopicsKnown(Arrays.asList(topicName));
	}

	public Map<Long, String> getTopics(String empGuid) {
		return employeeRepository.getTopics(empGuid);
	}

	public List<MiniTrainingDetail> getTrainings(String empGuid) {
		return employeeRepository.getTrainings(empGuid);
	}

	public boolean editTrainingField(String empGuid, SimpleFieldEntry simpleFieldEntry) throws ParseException {
		return employeeRepository.updateByFieldName(empGuid, simpleFieldEntry);
	}

	public void addTrainingTo(Map<String, String> topics, Training training) {
		if (topics != null && topics.size() > 0) {
			for (Map.Entry<String, String> topic : topics.entrySet()) {
				employeeRepository.addTraining(topic.getKey(), training);
			}
		}
	}

	public void removeTrainingFrom(Map<String, String> topics, Long trainingId) {
		if (topics != null && topics.size() > 0) {
			for (Map.Entry<String, String> topic : topics.entrySet()) {
				employeeRepository.removeTraining(topic.getKey().toString(), trainingId);
			}
		}
	}

	public JSONObject getEmployeesMinimal() {
		List<Employee> list = employeeRepository.findAllByEmailIdNotNull();
		JSONArray array = CommonUtil.getEmployeesMinimal(list);
		JSONObject obj = new JSONObject();
		obj.put("content", array);
		return obj;
	}

	public void updateTopics(Long topicId, String topicNewName) {
		employeeRepository.updateTopics(topicId, topicNewName);
	}

	public void updateTrainings(Long trainingId, String trainingNewName) {
		employeeRepository.updateTrainings(trainingId, trainingNewName);
	}
}
