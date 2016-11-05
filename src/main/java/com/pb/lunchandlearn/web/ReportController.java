package com.pb.lunchandlearn.web;

import com.pb.lunchandlearn.domain.Training;
import com.pb.lunchandlearn.service.EmployeeService;
import com.pb.lunchandlearn.service.TrainingService;
import com.pb.lunchandlearn.service.excel.EmployeesTrainingsExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DE007RA on 10/27/2016.
 */
@Controller
@RequestMapping(value = "/reports")
public class ReportController {
	@Autowired
	public TrainingService trainingService;
	@Autowired
	public EmployeeService employeeService;

	@RequestMapping(value = "status/completed/trainings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView trainingReports(@RequestParam(value = "search", required = false) String searchTerm,
										@RequestParam(value = "filterBy", required = false) String filterBy) {
		List<Training> trainings;
		if(StringUtils.isEmpty(filterBy)) {
			filterBy = "{\"status\": \"COMPLETED\"}";
		}
		else {
			int lastIndex = filterBy.lastIndexOf("}");
			if(lastIndex > -1) {
				filterBy = filterBy.substring(0, lastIndex - 1) + "\"status\": \"COMPLETED\"}";
			}
		}
		if (!StringUtils.isEmpty(searchTerm)) {
			trainings = trainingService.search(searchTerm, filterBy);
		}
		else {
			trainings = trainingService.getAll(filterBy);
		}
		Map<String, Object> values = new HashMap<>(4);
		values.put("trainings", trainings);
		values.put("employees", employeeService.getEmployeesMinimal());
		ModelAndView modelAndView = new ModelAndView(new EmployeesTrainingsExcelView(), values);
		return modelAndView;
	}
}