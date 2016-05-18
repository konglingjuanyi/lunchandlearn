package com.pb.lunchandlearn.web;

/**
 * Created by DE007RA on 4/27/2016.
 */
import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import com.pb.lunchandlearn.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	@Autowired
	public EmployeeService employeeService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Employee> list(Pageable pageable, @RequestParam(value = "search", required = false) String searchTerm) {
		if(!StringUtils.isEmpty(searchTerm)) {
			return employeeService.search(searchTerm, pageable);
		}
		return employeeService.getAll(pageable);
	}

	@RequestMapping(value = "/names", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Employee> listNames() {
		return employeeService.getAllNames();
	}

	@RequestMapping(value="/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long getCount() {
		return employeeService.getCount();
	}

	@RequestMapping(value="/employee/{guid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEmployee(@PathVariable("guid") String guid) {
		Employee employee = employeeService.getEmployee(guid);
		if(employee == null) {
			throw new ResourceNotFoundException(MessageFormat.format("Employee with given guid: {0} does not exist", guid));
		}
		return new ResponseEntity(employee, HttpStatus.OK);
	}

	@RequestMapping(value="/employee", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> addEmployee(@RequestBody Employee employee, BindingResult result) {
		if(!result.hasErrors()) {
			Employee newEmployee = employeeService.add(employee);
			HttpHeaders responseHeaders = new HttpHeaders();
			URI newEmployeeUri = ServletUriComponentsBuilder.
					fromCurrentRequest().path("/{guid}").buildAndExpand(newEmployee.getGuid()).toUri();
			responseHeaders.setLocation(newEmployeeUri);
			return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
		}
		return null;
	}

	@RequestMapping(value="/employee/{guid}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void editEmployee(@RequestBody  Employee employee) {
		employeeService.editEmployee(employee);
	}

	@RequestMapping(value="/employee/{guid}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteEmployee(@PathVariable("guid") String guid) {
		employeeService.deleteEmployee(guid);
	}

	@RequestMapping(value="/topic/{topicName}", method = RequestMethod.GET)
	public void employeesKnownToTopic(@PathVariable("topicName") String topicName) {
		employeeService.getEmployeesByTopic(topicName);
	}
}