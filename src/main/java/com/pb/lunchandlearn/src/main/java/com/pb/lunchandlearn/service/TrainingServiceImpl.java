package com.pb.lunchandlearn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pb.lunchandlearn.model.Employee;

@Service("traingingService")
public abstract class TrainingServiceImpl implements EmployeeService {

	List<Employee> emps = new ArrayList<Employee>();
//	@Autowired
//	EmployeeRepository employeeRepository;

/*	@PostConstruct
	void initEmps() {
		emps.add(new Employee("am001si", "Akhil Shrivastava", "akhil@pb.com"));
		emps.add(new Employee("vi001si", "Vijay Kumar", "vijay@pb.com"));
		emps.add(new Employee("am001si", "Tanmay", "tanmay@pb.com"));
		emps.add(new Employee("am001si", "Ravi", "ravi@pb.com"));
		emps.add(new Employee("am001si", "Rajneesh", "rajneesh@pb.com"));
	}

	@Override
	public Employee create(Employee emp) {
//		Employee savedEmp = employeeRepository.save(emp);
		emps.add(emp);
		Employee savedEmp = emps.get(emps.size() - 1);
		return savedEmp;
	}

	@Override
	public Employee findByGuid(String guid) {
		for(Employee emp : emps) {
			if(emp.getGuid().equalsIgnoreCase(guid)) {
				return emp;
			}
		}
//		employeeRepository.findByGuid(guid);
		return null;
	}

	@Override
	public void deleteByGuid(String guid) {
		Employee empToDelete = null;
		for(Employee emp : emps) {
			if(emp.getGuid().equalsIgnoreCase(guid)) {
				empToDelete = emp;
				break;
			}
		}
		emps.remove(empToDelete);
		//		employeeRepository.deleteByGuid(guid);
	}

	@Override
	public Iterable<Employee> list() {
//		return employeeRepository.findAll();
		return emps;
	}*/
}