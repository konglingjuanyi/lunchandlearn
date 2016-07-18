package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.config.ServiceAccountSettings;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.exception.InvalidOperationException;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import com.pb.lunchandlearn.repository.EmployeeRepository;
import com.pb.lunchandlearn.service.mail.MailService;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

import static com.pb.lunchandlearn.utils.CommonUtil.updateOldNewMapValues;

/**
 * Created by de007ra on 5/1/2016.
 */
@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	public static final List<String> MANAGER_ROLE_LIST = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(UserRole.MANAGER.name())));

	public static final List<String> ADMIN_ROLE_LIST = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(UserRole.ADMIN.name())));

	@Autowired
	private TopicService topicService;

	@Autowired
	private ServiceAccountSettings accountSettings;

	@Autowired
	private MailService mailService;

	public EmployeeService() {
	}

	@PostConstruct
	public void init() {
		try {
			employeeRepository.findByRoles(ADMIN_ROLE_LIST);
		} catch (ResourceNotFoundException exp) {
			//create service account
			try {
				Employee admin = employeeRepository.findByGuid(accountSettings.getGuid());
				List<String> roles = admin.getRoles();
				if (CollectionUtils.isEmpty(roles)) {
					roles = ADMIN_ROLE_LIST;
				} else {
					roles.add(UserRole.ADMIN.name());
				}
				updateField(admin.getGuid(), new SimpleFieldEntry("roles", roles));
			} catch (ResourceNotFoundException ex) {
				add(new Employee(accountSettings.getGuid(), accountSettings.getName(), accountSettings.getEmailId(), ADMIN_ROLE_LIST));
			} catch (Exception ex) {
				throw new InvalidOperationException("EmployeeServie.init()");
			}
		}
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
		return employeeRepository.findOne(empId.toUpperCase());
	}

	public void deleteEmployee(String empId) {
		employeeRepository.delete(empId.toUpperCase());
	}

	public Employee add(Employee employee) {
		return employeeRepository.insert(employee);
	}

	public Employee update(Employee employee) {
		employee.setGuid(employee.getGuid().toUpperCase());
		return employeeRepository.save(employee);
	}

	public List<Employee> getEmployeesByTopicKnown(String topicName) {
		return employeeRepository.findByTopicsKnown(Arrays.asList(topicName));
	}

	public Map<Long, String> getTopicsKnown(String empGuid) {
		return employeeRepository.getTopics(empGuid.toUpperCase(), "topicsKnown");
	}

	public Map<Long, String> getTopicsInterestedIn(String empGuid) {
		return employeeRepository.getTopics(empGuid.toUpperCase(), "topicsInterestedIn");
	}

	public List<MiniTrainingDetail> getTrainingsInterestedIn(String empGuid) {
		return employeeRepository.getTrainings(empGuid.toUpperCase(), "trainingsInterestedIn");
	}

	public List<MiniTrainingDetail> getTrainingsAttended(String empGuid) {
		return employeeRepository.getTrainings(empGuid.toUpperCase(), "trainingsAttended");
	}

	public List<MiniTrainingDetail> getTrainingsImparted(String empGuid) {
		return employeeRepository.getTrainings(empGuid.toUpperCase(), "trainingsImparted");
	}

	public boolean updateField(String empGuid, SimpleFieldEntry simpleFieldEntry) throws ParseException {
		Map<Object, Object> oldValues = null;
		switch (simpleFieldEntry.getName()) {
			case "topicsKnown":
				oldValues = (Map) employeeRepository.getTopics(empGuid.toUpperCase(), "topicsKnown");
				updateEntries(empGuid, simpleFieldEntry.getName(), oldValues, (Map) simpleFieldEntry.getValue());
				break;
			case "topicsInterestedIn":
				oldValues = (Map) employeeRepository.getTopics(empGuid.toUpperCase(), "topicsInterestedIn");
				updateEntries(empGuid, simpleFieldEntry.getName(), oldValues, (Map) simpleFieldEntry.getValue());
				break;
		}
		return employeeRepository.updateByFieldName(empGuid.toUpperCase(), simpleFieldEntry);
	}

	private void updateEntries(String empGuid, String fieldName, Map<Object, Object> oldEntries, Map<Object, Object> newEntries) {
		Map<Object, Object> addedEntries = null;
		addedEntries = updateOldNewMapValues(oldEntries, newEntries, addedEntries);
		switch (fieldName) {
			case "topicsKnown":
				if (oldEntries != null && oldEntries.size() > 0) {
					topicService.removeEmployees((Map) oldEntries, empGuid.toUpperCase(), "employeesKnowAbout");
				}
				if (addedEntries != null && addedEntries.size() > 0) {
					topicService.addEmployees((Map) addedEntries, employeeRepository.findByTheEmployeesId(empGuid.toUpperCase()),
							"employeesKnowAbout");
				}
				break;
			case "topicsInterestedIn":
				if (oldEntries != null && oldEntries.size() > 0) {
					topicService.removeEmployees((Map) oldEntries, empGuid.toUpperCase(), "interestedEmployees");
				}
				if (addedEntries != null && addedEntries.size() > 0) {
					topicService.addEmployees((Map) addedEntries, employeeRepository.findByTheEmployeesId(empGuid.toUpperCase()),
							"interestedEmployees");
				}
				break;
		}
	}

	public void addTrainingTo(Map<String, String> employees, Training training, String trainingStr) {
		if (employees != null && employees.size() > 0) {
			for (Map.Entry<String, String> emp : employees.entrySet()) {
				employeeRepository.addTraining(emp.getKey(), training, trainingStr);
			}
		}
	}

	public void removeTrainingFrom(Map<String, String> employees, Long trainingId, String trainingStr) {
		if (employees != null && employees.size() > 0) {
			for (Map.Entry<String, String> emp : employees.entrySet()) {
				employeeRepository.removeTraining(emp.getKey().toString(), trainingId, trainingStr);
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
		employeeRepository.updateTopics(topicId, topicNewName, "topicsKnown");
		employeeRepository.updateTopics(topicId, topicNewName, "topicsInterestedIn");
	}

	public void updateTrainings(Long trainingId, String trainingNewName) {
		employeeRepository.updateTrainings(trainingId, trainingNewName, "trainingsInterestedIn");
		employeeRepository.updateTrainings(trainingId, trainingNewName, "trainingsAttended");
		employeeRepository.updateTrainings(trainingId, trainingNewName, "trainingsImparted");
	}

	public boolean updateTopicInterestedIn(String guid, Long topicId, String topicName) {
		employeeRepository.addTopicInterested(guid.toUpperCase(), topicId, topicName);
		return true;
	}

	public User getUser(String guid) {
		Employee emp = employeeRepository.findByGuid(guid.toUpperCase());
		if (emp != null) {
			return emp;
		}
		throw new ResourceNotFoundException(MessageFormat.format("User with GUID: {} doesn't exist", guid));
	}

	public List<Employee> findByRoles(List<String> roles) {
		return employeeRepository.findAllByRoles(roles);
	}

	public JSONArray getAllManagers() {
		return CommonUtil.getEmployeesGuidName(employeeRepository.findAllByRoles(MANAGER_ROLE_LIST));
	}
}