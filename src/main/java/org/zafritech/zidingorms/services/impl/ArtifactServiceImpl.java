package org.zafritech.zidingorms.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.commons.enums.ItemClass;
import org.zafritech.zidingorms.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.dao.ItemDao;
import org.zafritech.zidingorms.dao.converter.DaoToItemConverter;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemComment;
import org.zafritech.zidingorms.domain.SystemVariable;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.ItemTypeRepository;
import org.zafritech.zidingorms.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.services.ArtifactService;
import org.zafritech.zidingorms.services.ItemService;
import org.zafritech.zidingorms.services.UserService;

@Service
public class ArtifactServiceImpl implements ArtifactService {

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private SystemVariableRepository sysVarRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;
    
    @Autowired
    private ItemCommentRepository commentRepository;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private DaoToItemConverter daoToItem;

    @Override
    public Item saveItemDao(ItemDao itemDao) {

        if (itemDao != null) {

            return itemRepository.save(daoToItem.convert(itemDao));
        }

        return null;
    }
    
    private static Map<String, CellStyle> styles;
    
    @Override
    public void initializeArtifact(Long id, String variableType) {

        switch (variableType) {

            case "ITEM_UUID_TEMPLATE":

                sysVarRepository.save(new SystemVariable(SystemVariableTypes.ITEM_UUID_TEMPLATE.name(), "ZID-REF", "DOCUMENT", id));
                break;

            case "REQUIREMENT_ID_TEMPLATE":

                sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), "SYS-COM", "DOCUMENT", id));
                sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), "SYS-GEN", "DOCUMENT", id));
                sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), "SYS-REL", "DOCUMENT", id));
                sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), "SYS-STA", "DOCUMENT", id));
                break;

            case "ITEM_UUID_NUMERIC_DIGITS":

                sysVarRepository.save(new SystemVariable(SystemVariableTypes.ITEM_UUID_NUMERIC_DIGITS.name(), "4", "DOCUMENT", id));
                break;

            case "REQUIREMENT_ID_NUMERIC_DIGITS":

                sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_NUMERIC_DIGITS.name(), "4", "DOCUMENT", id));
                break;

        }

    }

    @Override
    public Artifact importExcel(Long artifactId, String filePath) {

        Artifact artifact = artifactRepository.findOne(artifactId);
        
        int index = 0;  // Item sort index

        try {

            FileInputStream inputStream = new FileInputStream(new File(filePath));

            int i = 1;  // Skip header row, i = 0

            Workbook workbook = getWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);

//            while(i <= worksheet.getLastRowNum()) {
            while ((i <= 501) && (i <= worksheet.getLastRowNum())) {

                ItemDao itemDao = new ItemDao();
                Item item = new Item();
                Row row = worksheet.getRow(i++);

                // Item System ID
                itemDao.setSysId((String) getCellValue(row.getCell(0)));

                // Item Identifier
                String ident = (String) getCellValue(row.getCell(1));
                itemDao.setIdentifier((ident != null && !ident.isEmpty()) ? ident : "");

                // Is the Item a Requirement
                String itemClass = (String) getCellValue(row.getCell(2));
                itemDao.setItemClass((itemClass.equalsIgnoreCase("DEF")) ? ItemClass.REQUIREMENT.name() : itemClass);

                // Item value
                String value = (String) getCellValue(row.getCell(3));
                itemDao.setItemValue((value != null && !value.isEmpty()) ? value : "");

                // Set the Requirement type
                itemDao.setItemType((itemClass.equalsIgnoreCase("DEF") || itemClass.equalsIgnoreCase("REQUIREMENT")) 
                        ? itemTypeRepository.findByItemTypeName("Functional")
                        : itemTypeRepository.findByItemTypeName("None"));
                
                // Item level in hierachy
                int level = (int) (double) getCellValue(row.getCell(4));
                itemDao.setItemLevel(level);

                if (row.getCell(5).getCellTypeEnum() == CellType.NUMERIC) {
                    
                    int itemIndex = (int) (double) getCellValue(row.getCell(5));
                    itemDao.setSortIndex(itemIndex);
                    index = itemIndex;
                    
                } else {
                    
                    itemDao.setSortIndex(index++);
                }

                itemDao.setArtifactId(artifact.getId());
                item = this.saveItemDao(itemDao);
                
                // Item comments
                String clientComment = (row.getCell(6, Row.RETURN_BLANK_AS_NULL) != null) ? (String) getCellValue(row.getCell(6, Row.RETURN_BLANK_AS_NULL)) : "";
                String contractorComment = (row.getCell(7, Row.RETURN_BLANK_AS_NULL) != null) ? (String) getCellValue(row.getCell(7, Row.RETURN_BLANK_AS_NULL)) : "";
                
                // Save client comment
                if (clientComment != null && !clientComment.isEmpty()) {
                    
                    ItemComment comment = new ItemComment(item, clientComment, userService.findByEmail("client@zafritech.org"));
                    commentRepository.save(comment);
                    itemService.incrementCommentCount(item.getId());
                }
                
                // Save client comment
                if (contractorComment != null && !contractorComment.isEmpty()) {
                    
                    ItemComment comment = new ItemComment(item, contractorComment, userService.findByEmail("contractor@zafritech.org"));
                    commentRepository.save(comment);
                    itemService.incrementCommentCount(item.getId());
                }
            }

            workbook.close();
            inputStream.close();

        } catch (IOException e) {

        }

        return null;
    }

    @Override
    public XSSFWorkbook DownloadExcel(Long id) throws FileNotFoundException, IOException {
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        styles = createStyles(workbook);
        
        workbook = addRequirementsToWorksheet(workbook, id);
        workbook = addCommentsToWorksheet(workbook, id);
        workbook = addLinksToWorksheet(workbook, id);
            
        return workbook;
    }
    
    
    // Excel 2003 or 2007
    private Workbook getWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {

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
    
    private XSSFWorkbook addRequirementsToWorksheet(XSSFWorkbook workbook, Long id) {
        
        XSSFSheet reqSheet = workbook.createSheet("Requirements");
        
        // Column Widths
        reqSheet.setColumnWidth(0, 5000);
        reqSheet.setColumnWidth(1, 5000);
        reqSheet.setColumnWidth(2, 5000);
        reqSheet.setColumnWidth(3, 25000);
        reqSheet.setColumnWidth(4, 3000);
        reqSheet.setColumnWidth(5, 3000);
          
        Row headerRow = reqSheet.createRow(0);
        headerRow.setHeightInPoints(28);
        
        Cell cellSysId = headerRow.createCell(0);
        cellSysId.setCellValue("SYS_ID");
        cellSysId.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellIdent = headerRow.createCell(1);
        cellIdent.setCellValue("UNIQUE_ID");
        cellIdent.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellClass = headerRow.createCell(2);
        cellClass.setCellValue("TYPE");
        cellClass.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellValue = headerRow.createCell(3);
        cellValue.setCellValue("VALUE");
        cellValue.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellLevel = headerRow.createCell(4);
        cellLevel.setCellValue("LEVEL");
        cellLevel.setCellStyle((CellStyle) styles.get("HeaderCenterAlign"));
        
        Cell cellSortIdx = headerRow.createCell(5);
        cellSortIdx.setCellValue("INDEX");
        cellSortIdx.setCellStyle((CellStyle) styles.get("HeaderCenterAlign"));
        
        List<Item> items = itemRepository.findByArtifactId(id);
        
        int rowCount = 0;
        
        for (Item item : items ) {
            
            Row row = reqSheet.createRow(++rowCount);
            
            Cell cell0 = row.createCell(0);
            if (item.getSysId() instanceof String) { cell0.setCellValue(item.getSysId()); } 
            cell0.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
            
            Cell cell1 = row.createCell(1);
            if (item.getIdentifier() instanceof String) { cell1.setCellValue(item.getIdentifier()); } 
            cell1.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
            
            Cell cell2 = row.createCell(2);
            if (item.getItemClass() instanceof String) { cell2.setCellValue(item.getItemClass()); } 
            cell2.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
            
            Cell cell3 = row.createCell(3);
            if (item.getItemValue() instanceof String) { cell3.setCellValue(item.getItemValue()); } 
            cell3.setCellStyle((CellStyle) styles.get("BodyLeftAlignWrapText"));
                    
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(item.getItemLevel());
            cell4.setCellStyle((CellStyle) styles.get("BodyCenterAlign"));
            
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(item.getSortIndex());
            cell5.setCellStyle((CellStyle) styles.get("BodyCenterAlign"));
        }
        
        return workbook;
    }
        
    
    private XSSFWorkbook addCommentsToWorksheet(XSSFWorkbook workbook, Long id) {
        
        XSSFSheet commentSheet = workbook.createSheet("Comments");
        
        // Set Column Widths
        commentSheet.setColumnWidth(0, 3000);   // id
        commentSheet.setColumnWidth(1, 5000);   // SysId
        commentSheet.setColumnWidth(2, 25000);  // Comment
        commentSheet.setColumnWidth(3, 5000);   // Date
        commentSheet.setColumnWidth(4, 5000);   // Author
 
        // Set Header Row Height
        Row headerRow = commentSheet.createRow(0);
        headerRow.setHeightInPoints(28);
        
        // Populate Header Row
        Cell cellId = headerRow.createCell(0);
        cellId.setCellValue("ID");
        cellId.setCellStyle((CellStyle) styles.get("HeaderCenterAlign")); 
        
        Cell cellSysId = headerRow.createCell(1);
        cellSysId.setCellValue("SYS_ID");
        cellSysId.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellComment = headerRow.createCell(2);
        cellComment.setCellValue("COMMENT");
        cellComment.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellDate = headerRow.createCell(3);
        cellDate.setCellValue("DATE");
        cellDate.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellAuthor = headerRow.createCell(4);
        cellAuthor.setCellValue("AUTHOR");
        cellAuthor.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        List<Item> items = itemRepository.findByArtifactId(id);
        
        int rowCount = 0;
        
        for (Item item : items ) {
        
            List<ItemComment> comments = commentRepository.findByItemIdOrderByCreationDateDesc(item.getId());
             
            for (ItemComment comment : comments ) {
                 
                User user = userService.findById(comment.getAuthor().getId());
                
                Row row = commentSheet.createRow(++rowCount);
                 
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(comment.getId());
                cell0.setCellStyle((CellStyle) styles.get("BodyCenterAlign"));
                
                Cell cell1 = row.createCell(1);
                if (item.getSysId() instanceof String) { cell1.setCellValue(item.getSysId()); } 
                cell1.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
                
                Cell cell2 = row.createCell(2);
                if (comment.getComment() instanceof String) { cell2.setCellValue(comment.getComment()); } 
                cell2.setCellStyle((CellStyle) styles.get("BodyLeftAlignWrapText"));
                
                Cell cell3 = row.createCell(3);
                if (comment.getCreationDate() instanceof Date) { cell3.setCellValue(comment.getCreationDate()); } 
                cell3.setCellStyle((CellStyle) styles.get("BodyLeftAlignDate"));
                
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(user.getFirstName()); 
                cell4.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
             }
         }
         
        return workbook;
    }

    
    private XSSFWorkbook addLinksToWorksheet(XSSFWorkbook workbook, Long id) {
        
        XSSFSheet linkSheet = workbook.createSheet("Links");
        
        // Set Column Widths
        linkSheet.setColumnWidth(0, 3000);   // id
        linkSheet.setColumnWidth(1, 6000);   // Unique Id
        linkSheet.setColumnWidth(2, 6000);  // Src
        linkSheet.setColumnWidth(3, 6000);   // Dst
        linkSheet.setColumnWidth(4, 6000);   // Group
        linkSheet.setColumnWidth(5, 6000);   // Type
        linkSheet.setColumnWidth(6, 5000);   // Date
        
        Row headerRow = linkSheet.createRow(0);
        headerRow.setHeightInPoints(28);
        
        // Populate Header Row
        Cell cellId = headerRow.createCell(0);
        cellId.setCellValue("ID");
        cellId.setCellStyle((CellStyle) styles.get("HeaderCenterAlign")); 
        
        Cell cellUniqueId = headerRow.createCell(1);
        cellUniqueId.setCellValue("UNIQUE_ID");
        cellUniqueId.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellSource = headerRow.createCell(2);
        cellSource.setCellValue("LINK SRC");
        cellSource.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellDestination = headerRow.createCell(3);
        cellDestination.setCellValue("LINK DST");
        cellDestination.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellGroup = headerRow.createCell(4);
        cellGroup.setCellValue("LINK GROUP");
        cellGroup.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellType = headerRow.createCell(5);
        cellType.setCellValue("LINK TYPE");
        cellType.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellDate = headerRow.createCell(6);
        cellDate.setCellValue("DATE");
        cellDate.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        return workbook;
    }
    
    
    @SuppressWarnings("deprecation")
    private Object getCellValue(Cell cell) {

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