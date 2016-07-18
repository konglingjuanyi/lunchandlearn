package com.pb.lunchandlearn.repository;

import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.domain.TrainingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by de007ra on 5/1/2016.
 */
@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String>, CustomEmployeeRepository{

	Employee findByEmailId(String emailId);
	List<Employee> findByTopicsKnown(List<String> topicName);

	@Query(fields = "{'name': 1, 'guid': 1}")
	List<Employee> findAllByNameNotNull();

	@Query(fields = "{'name': 1, 'guid': 1, 'emailId': 1}")
	List<Employee> findAllByEmailIdNotNull();

	@Query(fields = "{'name': 1, 'guid': 1, 'emailId': 1, 'roles': 1}")
	Employee findByGuid(String guid);

	Page<Training> findAllByStatusOrderByScore(TrainingStatus status, TextCriteria textCriteria, Pageable pageable);

	Page<Employee> findAllBy(TextCriteria textCriteria, Pageable pageable);

	@Query(value="{ 'guid' : ?0 }", fields="{ 'name' : 1}")
	Employee findByTheEmployeesId(String empGuid);

	@Query(fields="{ 'guid' : 1}")
	Employee findByRoles(List<String> adminRole);

	@Query(fields="{ 'guid' : 1, 'emailId': 1}")
	List<Employee> findAllByGuidIn(List<String> guids);
}