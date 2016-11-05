package com.pb.lunchandlearn.service.excel;

public class ItemsExcelView {/*extends AbstractXlsxView {

	static final String[] ippitemRowHeaderColsNames = { "Name", "Type", "Cost", "Sorted", "Enabled" };
	static final String[] itemRowHeaderColsNames = { "Name", "Enabled" };
	static final String[] projectRowHeaderColsNames = { "Name", "CC Description", "Line Of Business", "Enabled" };
	static final String[] costCenterRowHeaderColsNames = { "CC Description", "Code", "Cost Used", "Enabled" };
	HSSFCellStyle cellStyleDefault = null;

	// get locations from excel
	public static List<Item> readLocations(final String fileFullPath) throws IOException {
		List<Item> locations = new ArrayList<Item>();
		HSSFSheet mySheet = null;
		int totalRows = 0;
		HSSFWorkbook myWorkBook = ExcelUtil.getWorkSheet(fileFullPath);
		mySheet = myWorkBook.getSheetAt(0);
		totalRows = mySheet.getPhysicalNumberOfRows();
		String name = null;
		boolean enabled = true;
		int rowCount = 1;
		while (rowCount < totalRows) {
			HSSFRow row = mySheet.getRow(rowCount);
			name = StringUtils.trim(ExcelUtil.getCellValue(row.getCell(0)));
			enabled = "Yes".equalsIgnoreCase(StringUtils.trim(ExcelUtil.getCellValue(row.getCell(1))));

			locations.add(new Item(null, name, enabled, ItemType.LOCATION, UserServiceImpl.SYSTEM_USER));
			++rowCount;
		}
		myWorkBook.close();
		return locations;
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		@SuppressWarnings("unchecked")
		List<Item> items = (List<Item>) model.get("items");
		HSSFSheet workSheet = workbook.createSheet("Sheet1");
		if (!CollectionUtils.isEmpty(items)) {
			ItemType itemType = items.get(0).getItemType();
			String[] rowHeaderColsNames = getItemRow(itemType);
			ExcelUtil.setHeaderRow(workbook, workSheet, rowHeaderColsNames);

			int colCount = 0;
			HSSFRow row = null;
			cellStyleDefault = ExcelUtil.getDefaultCellStyle(workbook);

			HSSFFont font = workbook.createFont();
			font.setColor(HSSFColor.GREEN.index);

			for (int rowCount = 0; rowCount < items.size(); ++rowCount) {
				colCount = 0;
				Item item = items.get(rowCount);
				row = workSheet.createRow(rowCount + 1);

				HSSFCell cell = row.createCell(colCount++);
				cell.setCellValue(item.getName());
				cell.setCellStyle(cellStyleDefault);

				if (ItemType.IPP_ITEM.equals(itemType)) {
					IPPItem ippItem = (IPPItem) item;
					IPPItemType ippItemType = ippItem.getType();
					cell = row.createCell(colCount++);
					Set<String> subItems = ippItem.getSubitems();
					if (IPPItemType.CUSTOM.equals(ippItemType) && !CollectionUtils.isEmpty(subItems)) {
						StringBuffer sb = new StringBuffer(IPPItemType.CUSTOM.toString());
						sb.append("(");
						boolean firstItem = true;
						for (String subItem : subItems) {
							if (!firstItem) {
								sb.append(", ");
							} else {
								firstItem = false;
							}
							sb.append(subItem);
						}
						sb.append(")");
						cell.setCellValue(sb.toString());
					} else {
						if(ippItemType != null) {
							cell.setCellValue(ippItemType.toString());
						}
					}
					cell.setCellStyle(cellStyleDefault);
					cell = row.createCell(colCount++);
					ExcelUtil.setDoubleValue(cell, ippItem.getCost());
					cell.setCellStyle(cellStyleDefault);
					cell = row.createCell(colCount++);
					cell.setCellValue(ExcelUtil.getBooleanString(ippItem.isSorted()));
					cell.setCellStyle(cellStyleDefault);

				} else if (ItemType.PROJECT.equals(itemType)) {
					Project project = (Project) item;
					cell = row.createCell(colCount++);
					cell.setCellValue(project.getCostCenter());
					cell.setCellStyle(cellStyleDefault);
					cell = row.createCell(colCount++);
					cell.setCellValue(project.getLob());
					cell.setCellStyle(cellStyleDefault);
				} else if (ItemType.COST_CENTER.equals(itemType)) {
					CostCenter costCenter = (CostCenter) item;
					cell = row.createCell(colCount++);
					cell.setCellValue(costCenter.getCode());
					cell.setCellStyle(cellStyleDefault);
					cell = row.createCell(colCount++);
					cell.setCellValue(costCenter.getCostUsed());
					cell.setCellStyle(cellStyleDefault);
				}

				cell = row.createCell(colCount++);
				cell.setCellValue(ExcelUtil.getBooleanString(item.isEnabled()));
				cell.setCellStyle(cellStyleDefault);
			}
			ExcelUtil.setColumnsAutoSize(workSheet, row.getPhysicalNumberOfCells() - 1);
			ExcelUtil.setLastRowBorder(workbook, row);// last row and
															// colCount
		}
	}

	public static List<IPPItem> readIPPItems(final String fullFilePath) throws IOException {
		List<IPPItem> ippItems = new ArrayList<IPPItem>();
		HSSFSheet mySheet = null;
		int totalRows = 0;
		HSSFWorkbook myWorkBook = ExcelUtil.getWorkSheet(fullFilePath);
		mySheet = myWorkBook.getSheetAt(0);
		totalRows = mySheet.getPhysicalNumberOfRows();
		int rowCount = 1;
		HashSet<String> subItems = new HashSet<String>();
		while (rowCount < totalRows) {
			HSSFRow row = mySheet.getRow(rowCount);
			String name = StringUtils.trim(ExcelUtil.getCellValue(row.getCell(0)));
			String strType = StringUtils.trim(ExcelUtil.getCellValue(row.getCell(1)));
			String strCost = StringUtils.trim(ExcelUtil.getCellValue(row.getCell(2)));
			Double cost = null;
			if (!StringUtils.isEmpty(strCost)) {
				cost = Double.parseDouble(strCost);
			}
			boolean sorted = "Yes".equalsIgnoreCase(StringUtils.trim(ExcelUtil.getCellValue(row.getCell(3))));
			boolean enabled = "Yes".equalsIgnoreCase(StringUtils.trim(ExcelUtil.getCellValue(row.getCell(4))));

			IPPItem ippItem = new IPPItem(null, name, enabled, null, UserServiceImpl.SYSTEM_USER, cost, sorted);
			if (strType.startsWith(IPPItemType.CUSTOM.toString())) {
				String strSubItems = strType.substring(strType.indexOf("(") + 1, strType.lastIndexOf(")"));
				String[] items = strSubItems.split(", ");
				subItems = new HashSet<String>(items.length);
				for (String item : items) {
					subItems.add(item);
				}
				ippItem.setType(IPPItemType.CUSTOM);
			} else {
				IPPItemType type = (IPPItem.IPPItemType) CommonAdapter.toEnum(
						StringUtils.trim(ExcelUtil.getCellValue(row.getCell(1)).toUpperCase()),
						IPPItem.IPPItemType.values());
				ippItem.setType(type);
			}

			ippItem.setSubitems(subItems);
			ippItems.add(ippItem);
			++rowCount;
		}
		myWorkBook.close();
		return ippItems;
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

	}
*/}