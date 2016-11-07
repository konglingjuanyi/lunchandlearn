package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by de007ra on 5/1/2016.
 */
public interface EmployeeRepository extends MongoRepository<Employee, String>, CustomEmployeeRepository {

	Employee findByEmailId(String emailId);
	List<Employee> findByTopicsKnown(List<String> topicName);

	@Query(fields = "{'name': 1, 'guid': 1}")
	List<Employee> findAllByNameNotNull();

	@Query(fields = "{'name': 1, 'guid': 1, 'emailId': 1}")
	List<Employee> findAllByEmailIdNotNull();

	@Query(fields = "{'name': 1, 'guid': 1, 'emailId': 1, 'roles': 1, 'managers': 1}")
	Employee findByGuid(String guid);

	Page<Employee> findAllBy(TextCriteria textCriteria, Pageable pageable);

	@Query(value="{ 'guid' : ?0 }", fields="{ 'name' : 1}")
	Employee findByTheEmployeesId(String empGuid);

	@Query(fields="{ 'guid' : 1}")
	Employee findByRoles(List<String> adminRole);

	@Query(fields="{ 'guid' : 1, 'emailId': 1}")
	Collection<Employee> findAllByGuidIn(Collection<String> guids);
}