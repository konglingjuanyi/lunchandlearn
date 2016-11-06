package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.domain.*;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import com.pb.lunchandlearn.repository.EmployeeRepository;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

import static com.pb.lunchandlearn.config.SecurityConfig.getLoggedInUser;
import static com.pb.lunchandlearn.utils.CommonUtil.updateOldNewMapValues;

/**
 * Created by de007ra on 5/1/2016.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private LDAPService ldapService;

	public static final List<String> MANAGER_ROLE_LIST = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(UserRole.MANAGER.name())));

	public static final List<String> ADMIN_ROLE_LIST = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(UserRole.ADMIN.name())));

	public static final List<String> CLERICAL_ROLE_LIST = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(UserRole.CLERICAL.name())));

	@Autowired
	private TopicService topicService;

	public EmployeeServiceImpl() {
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Page<Employee> getAll(Pageable pageable) {
		SecuredUser user = getLoggedInUser();
		if(user.isAdmin()) {
			return employeeRepository.findAll(pageable);
		}
		else if(user.isManager()) {
			return employeeRepository.findAllExistsByManagers(user.getGuid(), pageable);
		}
		return null;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Page<Employee> search(String term, Pageable pageable) {
		TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(term);
		return employeeRepository.findAllBy(textCriteria, pageable);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public List<Employee> getAllNames() {
		return employeeRepository.findAllByNameNotNull();
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Long getCount() {
		return employeeRepository.count();
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Employee getEmployee(String empId) {
		return employeeRepository.findOne(empId.toUpperCase());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteEmployee(String empId) {
		employeeRepository.delete(empId.toUpperCase());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public Employee add(Employee employee) {
		return employeeRepository.insert(employee);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Employee update(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public List<Employee> getEmployeesByTopicKnown(String topicName) {
		return employeeRepository.findByTopicsKnown(Arrays.asList(topicName));
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Map<Long, String> getTopicsKnown(String empGuid) {
		return employeeRepository.getTopics(empGuid.toUpperCase(), "topicsKnown");
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public Map<Long, String> getTopicsInterestedIn(String empGuid) {
		return employeeRepository.getTopics(empGuid.toUpperCase(), "topicsInterestedIn");
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public List<MiniTrainingDetail> getTrainingsInterestedIn(String empGuid) {
		return employeeRepository.getTrainings(empGuid.toUpperCase(), "trainingsInterestedIn");
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public List<MiniTrainingDetail> getTrainingsAttended(String empGuid) {
		return employeeRepository.getTrainings(empGuid.toUpperCase(), "trainingsAttended");
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public List<MiniTrainingDetail> getTrainingsImparted(String empGuid) {
		return employeeRepository.getTrainings(empGuid.toUpperCase(), "trainingsImparted");
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
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

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public void addTrainingTo(Map<String, String> employees, Training training, String trainingStr) {
		if (employees != null && employees.size() > 0) {
			for (Map.Entry<String, String> emp : employees.entrySet()) {
				employeeRepository.addTraining(emp.getKey(), training, trainingStr);
			}
		}
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public void removeTrainingFrom(Map<String, String> employees, Long trainingId, String trainingStr) {
		if (employees != null && employees.size() > 0) {
			for (Map.Entry<String, String> emp : employees.entrySet()) {
				employeeRepository.removeTraining(emp.getKey().toString(), trainingId, trainingStr);
			}
		}
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public JSONObject getEmployeesMinimal() {
		List<Employee> list = employeeRepository.findAllByEmailIdNotNull();
		JSONArray array = CommonUtil.getEmployeesMinimal(list);
		JSONObject obj = new JSONObject();
		obj.put("content", array);
		return obj;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public List<Employee> getEmployeesWithNotNullEmailId() {
		List<Employee> list = employeeRepository.findAllByEmailIdNotNull();
		return list;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public void updateTopics(Long topicId, String topicNewName) {
		employeeRepository.updateTopics(topicId, topicNewName, "topicsKnown");
		employeeRepository.updateTopics(topicId, topicNewName, "topicsInterestedIn");
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public void updateTrainings(Long trainingId, String trainingNewName) {
		employeeRepository.updateTrainings(trainingId, trainingNewName, "trainingsInterestedIn");
		employeeRepository.updateTrainings(trainingId, trainingNewName, "trainingsAttended");
		employeeRepository.updateTrainings(trainingId, trainingNewName, "trainingsImparted");
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public boolean updateTopicInterestedIn(String guid, Long topicId, String topicName) {
		employeeRepository.addTopicInterested(guid.toUpperCase(), topicId, topicName);
		return true;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public User getUser(String guid) {
		Employee emp = employeeRepository.findByGuid(guid.toUpperCase());
		if (emp != null) {
			return emp;
		}
		throw new ResourceNotFoundException(MessageFormat.format("User with GUID: {} doesn't exist", guid));
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public List<Employee> findByRoles(List<String> roles) {
		return employeeRepository.findAllByRoles(roles);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public JSONArray getAllManagers() {
		return CommonUtil.getEmployeesGuidName(employeeRepository.findAllByRoles(MANAGER_ROLE_LIST));
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public void updatedLdapEmployees() throws Exception {
		ldapService.updateEmployees();
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public void updatedLdapEmployee(String guid) {
		ldapService.updateEmployee(guid);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public Long countByManagers(String guid) {
		return employeeRepository.countByManagers(guid);
	}

	@Override
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}
}