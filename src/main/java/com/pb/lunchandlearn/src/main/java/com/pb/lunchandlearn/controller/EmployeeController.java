package com.pb.lunchandlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pb.lunchandlearn.service.EmployeeService;

/**
 * Created by DE007RA on 7/28/2015.
 */
@RestController("employeeController")
@RequestMapping("employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

/*	@RequestMapping(value = "/{guid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String add(
			@PathVariable(value = "guid") String guid, @RequestBody Employee emp) {
		employeeService.create(emp);
		return "greeting";
	}

	@RequestMapping(value = "/{guid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Employee get(@PathVariable(value = "guid") String guid) {
		Employee emp = employeeService.findByGuid(guid);
		return emp;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<Employee> list() {
		return employeeService.list();
	}

	@RequestMapping(value = "/{guid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean remove(@PathVariable("guid") String guid) {
		employeeService.deleteByGuid(guid);
		return Boolean.TRUE;
	}*/
}