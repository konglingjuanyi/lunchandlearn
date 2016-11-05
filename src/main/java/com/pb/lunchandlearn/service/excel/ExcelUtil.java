package com.pb.lunchandlearn.service.excel;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

public class ExcelUtil {

	public static final BorderStyle cellBorder = BorderStyle.THIN;

	public static void setColumnsAutoSize(Sheet workSheet, int colCount) {
		// setAutoSize of cols
		while (colCount >= 0) {
			workSheet.autoSizeColumn(colCount--);
		}
	}

	public static CellStyle getCellHeaderStyle(Workbook workbook, HorizontalAlignment horizontalAlignment, boolean isBold) {
		CellStyle headerRowCellStyle = workbook.createCellStyle();
		headerRowCellStyle.setAlignment(horizontalAlignment);
		headerRowCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerRowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerRowCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		headerRowCellStyle.setFillBackgroundColor(HSSFColor.BLACK.index);
		headerRowCellStyle.setWrapText(true);
		headerRowCellStyle.setBorderLeft(cellBorder);
		headerRowCellStyle.setBorderRight(cellBorder);
		headerRowCellStyle.setBorderTop(cellBorder);
		headerRowCellStyle.setBorderBottom(cellBorder);
		if(isBold) {
			Font font = workbook.createFont();
			font.setBold(true);
			headerRowCellStyle.setFont(font);
		}
		return headerRowCellStyle;
	}

	public static CellStyle getHyperLinkCellStyle(Workbook workbook) {
		CellStyle hlinkCellStyle = workbook.createCellStyle();
		hlinkCellStyle.cloneStyleFrom(getDefaultCellStyle(workbook));
		Font hlinkFont = workbook.createFont();
		hlinkFont.setUnderline(Font.U_SINGLE);
		hlinkFont.setColor(HSSFColor.BLUE.index);
		hlinkCellStyle.setFont(hlinkFont);
		return hlinkCellStyle;
	}

	public static CellStyle getHyperLinkWithHeaderCellStyle(Workbook workbook, HorizontalAlignment horizontalAlignment) {
		CellStyle hlinkCellStyle = workbook.createCellStyle();
		hlinkCellStyle.cloneStyleFrom(getDefaultCellStyle(workbook));
		Font hlinkFont = workbook.createFont();
		hlinkFont.setUnderline(Font.U_SINGLE);
		hlinkFont.setColor(HSSFColor.BLUE.index);
		hlinkCellStyle.setFont(hlinkFont);

		hlinkCellStyle.setAlignment(horizontalAlignment);
		hlinkCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		hlinkCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		hlinkCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		hlinkCellStyle.setFillBackgroundColor(HSSFColor.BLACK.index);
		hlinkCellStyle.setWrapText(true);
		hlinkCellStyle.setBorderLeft(cellBorder);
		hlinkCellStyle.setBorderRight(cellBorder);
		hlinkCellStyle.setBorderTop(cellBorder);
		hlinkCellStyle.setBorderBottom(cellBorder);
		return hlinkCellStyle;
	}

	public static CellStyle getDefaultCellStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setBorderLeft(ExcelUtil.cellBorder);
		cellStyle.setBorderRight(ExcelUtil.cellBorder);
		return cellStyle;
	}

	public static Hyperlink getUrlHyperLink(CreationHelper createHelper, String toUrl) {
		Hyperlink link = createHelper.createHyperlink(HyperlinkType.EMAIL);
		//note, if subject contains white spaces, make sure they are url-encoded
		link.setAddress(toUrl);
		return link;
	}
}