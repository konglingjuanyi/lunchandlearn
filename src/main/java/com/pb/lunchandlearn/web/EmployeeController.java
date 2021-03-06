package com.pb.lunchandlearn.web;

/**
 * Created by DE007RA on 4/27/2016.
 */
import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.domain.SimpleFieldEntry;
import com.pb.lunchandlearn.service.EmployeeServiceImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

import java.net.URI;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	@Autowired
	public EmployeeServiceImpl employeeService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Employee> list(Pageable pageable, @RequestParam(value = "search", required = false) String searchTerm) {
		if(!StringUtils.isEmpty(searchTerm)) {
			return employeeService.search(searchTerm, pageable);
		}
		return employeeService.getAll(pageable);
	}

	@RequestMapping(value = "/ldap", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void refreshEmployees() throws Exception {
		employeeService.updatedLdapEmployees();
	}

	@RequestMapping(value = "/employee/{guid}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void refreshEmployee(@PathVariable("guid")String guid) throws Exception {
		employeeService.updatedLdapEmployee(guid);
	}

	@RequestMapping(value = "/minimal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject minimalList() {
		return employeeService.getEmployeesMinimal();
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public long count() {
		return employeeService.getCount();
	}

	@RequestMapping(value = "/names", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Employee> listNames() {
		return employeeService.getAllNames();
	}

	@RequestMapping(value = "/managers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray listManagers() {
		return employeeService.getAllManagers();
	}

	@RequestMapping(value="/employee/{guid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Employee getEmployee(@PathVariable("guid") String guid) {
		return employeeService.getEmployee(guid);
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
	public void updateEmployee(@RequestBody  Employee employee) {
		employeeService.update(employee);
	}

	@RequestMapping(value="/employee/{guid}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteEmployee(@PathVariable("guid") String guid) {
		employeeService.deleteEmployee(guid);
	}

	@RequestMapping(value="/topic/{topicName}", method = RequestMethod.GET)
	public void employeesKnownToTopic(@PathVariable("topicName") String topicName) {
		employeeService.getEmployeesByTopicKnown(topicName);
	}

	@RequestMapping(value = "/employee/{guid}/field", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void updateTrainingByField(@PathVariable("guid") String employeeGuid,
									  @RequestBody SimpleFieldEntry fieldEntry) throws ParseException {
		employeeService.updateField(employeeGuid, fieldEntry);
	}
}