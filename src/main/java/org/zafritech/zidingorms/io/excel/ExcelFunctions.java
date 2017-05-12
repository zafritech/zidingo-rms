/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.io.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 *
 * @author LukeS
 */
@Component
public class ExcelFunctions {
    
    // Excel 2003 or 2007
    public Workbook getExcelWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {

        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {

            workbook = new XSSFWorkbook(inputStream);

        } else if (excelFilePath.endsWith("xls")) {

            workbook = new HSSFWorkbook(inputStream);

        } else {

            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }
     
    @SuppressWarnings("deprecation")
    public Object getExcelCellValue(Cell cell) {

        switch (cell.getCellTypeEnum()) {

            case STRING:
                return cell.getStringCellValue();

            case BOOLEAN:
                return cell.getBooleanCellValue();

            case NUMERIC:
                return cell.getNumericCellValue();

            default:
                return null;
        }
    }
    
    public Map<String, CellStyle> getExcelStyles(Workbook wb) {
        
        return createStyles(wb);
    }
    
    private static Map<String, CellStyle> createStyles(Workbook wb) {
        
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CreationHelper creationHelper = wb.getCreationHelper();
        
        CellStyle style;
        
        // Header Font
        Font headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short)12);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex()); 
        
        // Header Left Aligned Style
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT); 
        style.setVerticalAlignment(VerticalAlignment.CENTER); 
        style.setFont(headerFont);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.put("HeaderLeftAlign", style);
        
        // Header Center Aligned Style
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER); 
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(headerFont);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.put("HeaderCenterAlign", style);
        
        // Body Left Aligned Style
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT); 
        style.setVerticalAlignment(VerticalAlignment.TOP);
        styles.put("BodyLeftAlign", style);
        
        // Body Center Aligned Style
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER); 
        style.setVerticalAlignment(VerticalAlignment.TOP);
        styles.put("BodyCenterAlign", style);
        
        // Body Left Aligned WrapText Style
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT); 
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setWrapText(true); 
        styles.put("BodyLeftAlignWrapText", style);
        
        // Body Left Aligned Date Format Style
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT); 
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss")); 
        styles.put("BodyLeftAlignDate", style);
        
        // Body Center Aligned Date Format Style
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER); 
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss")); 
        styles.put("BodyCenterAlignDate", style);
        
        return styles;
    }
   
    private static CellStyle createBorderedStyle(Workbook wb) {
        
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        
        return style;
    }
}
