package com.pb.lunchandlearn.service.excel;

import com.pb.lunchandlearn.config.ApplicationConfiguration;
import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.domain.Training;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pb.lunchandlearn.utils.CommonUtil.getDate;

/**
 * Created by DE007RA on 10/24/2016.
 */
@Component
public class EmployeesTrainingsExcelView extends AbstractXlsxView {
	private List<Training> trainings = null;
	private List<Employee> employees = null;
	private int rowCount = 0;
	private static Comparator<Employee> employeeComparator;
	private static Comparator<Training> trainingComparator;
	private CellStyle headerRightStyle;
	private CellStyle centerStyle;
	private CellStyle headerLeftStyle;
	private CellStyle hyperLinkStyle;
	private CellStyle hyperLinkHeaderStyle;
	private List<Training> trainingsPerCols;

	static {
		employeeComparator = (e1, e2) -> e1.getName().toLowerCase().compareTo(e2.getName().toLowerCase());
		trainingComparator = (t1, t2) -> t1.getScheduledOn().compareTo(t2.getScheduledOn());
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		trainings = (List<Training>) model.get("trainings");
		employees = (List<Employee>) model.get("employees");
		Sheet sheet = workbook.createSheet("TrainingDetails");
		CreationHelper creationHelper = workbook.getCreationHelper();
		headerRightStyle = ExcelUtil.getCellHeaderStyle(workbook, HorizontalAlignment.RIGHT, true);
		headerLeftStyle = ExcelUtil.getCellHeaderStyle(workbook, HorizontalAlignment.LEFT, true);
		centerStyle = ExcelUtil.getCellHeaderStyle(workbook, HorizontalAlignment.CENTER, false);
		hyperLinkStyle = ExcelUtil.getHyperLinkCellStyle(workbook);
		hyperLinkHeaderStyle = ExcelUtil.getHyperLinkWithHeaderCellStyle(workbook, HorizontalAlignment.CENTER);
		trainingsPerCols = new ArrayList<>(trainings.size());

		if (CollectionUtils.isNotEmpty(employees) && CollectionUtils.isNotEmpty(trainings)) {
			employees = employees.stream().sorted(employeeComparator).collect(Collectors.toList());
			trainings = trainings.stream().sorted(trainingComparator).collect(Collectors.toList());
			addTrainingDetailsRows(sheet, creationHelper);
			Row empRow = sheet.createRow(rowCount++);
			Cell cell = empRow.createCell(0);
			cell.setCellStyle(headerLeftStyle);
			cell.setCellValue("Trainees");
			for (Employee emp : employees) {
				if (StringUtils.isNotEmpty(emp.getEmailId())) {
					empRow = sheet.createRow(rowCount++);
					cell = empRow.createCell(0);
					cell.setHyperlink(ExcelUtil.getUrlHyperLink(creationHelper,
							ApplicationConfiguration.getEmployeeLink(emp)));
					cell.setCellStyle(hyperLinkStyle);
					cell.setCellValue(emp.getName());
					addTraineesDetails(emp, empRow);
				}
			}
			ExcelUtil.setColumnsAutoSize(sheet, 0);
		}
	}

	private void addTraineesDetails(Employee employee, Row trainerRow) {
		for (Training training : trainingsPerCols) {
			Cell trainerTrainingCell = trainerRow.createCell(1);
			if (training.getTrainers().containsKey(employee.getGuid())) {
				trainerTrainingCell.setCellValue(1);
			}
		}
	}

/*
	private String getTrainerDetails(Map.Entry<String, String> trainer) {
		StringBuffer sb = new StringBuffer();
		sb.append(trainer.getValue()).append(" (").append(trainer.getKey()).append(")");
		return sb.toString();
	}

*/
	private void addTrainingDetailsRows(Sheet sheet, CreationHelper creationHelper) {
		if (CollectionUtils.isNotEmpty(employees)) {
			Row trainingNameRow = sheet.createRow(rowCount++);
			Row trainingTrainersRow = sheet.createRow(rowCount++);
			Row trainingDateRow = sheet.createRow(rowCount++);
			Cell colHeading = trainingNameRow.createCell(0);
			colHeading.setCellValue("Training Title");
			colHeading.setCellStyle(headerRightStyle);
			colHeading = trainingTrainersRow.createCell(0);
			colHeading.setCellValue("Trainer");
			colHeading.setCellStyle(headerRightStyle);
			colHeading = trainingDateRow.createCell(0);
			colHeading.setCellValue("Scheduled On");
			colHeading.setCellStyle(headerRightStyle);

			int col = 1;
			for (Training training : trainings) {
				if (!training.getTrainers().isEmpty()) {
					for (String trainer : training.getTrainers().values()) {
						Cell nameCell = trainingNameRow.createCell(col);
						Cell trainerCell = trainingTrainersRow.createCell(col);
						Cell dateCell = trainingDateRow.createCell(col);

						nameCell.setHyperlink(ExcelUtil.getUrlHyperLink(creationHelper,
								ApplicationConfiguration.getTrainingLink(training)));
						nameCell.setCellValue(training.getName());
						nameCell.setCellStyle(hyperLinkHeaderStyle);
						trainerCell.setCellStyle(centerStyle);
						dateCell.setCellValue(getDate(training.getScheduledOn()));
						trainerCell.setCellValue(trainer);
						trainingsPerCols.add(training);
						col++;
					}
				}
			}
			while(col > 0) {
				ExcelUtil.setColumnsAutoSize(sheet, col);
				col--;
			}
		}
	}
}