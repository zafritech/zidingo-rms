package org.zafritech.zidingorms.items.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.commons.enums.ItemClass;
import org.zafritech.zidingorms.core.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.database.dao.ItemDao;
import org.zafritech.zidingorms.database.dao.converter.DaoToItemConverter;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.ItemComment;
import org.zafritech.zidingorms.database.domain.SystemVariable;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.ArtifactRepository;
import org.zafritech.zidingorms.database.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.database.repositories.ItemRepository;
import org.zafritech.zidingorms.database.repositories.ItemTypeRepository;
import org.zafritech.zidingorms.database.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.io.excel.ExcelFunctions;
import org.zafritech.zidingorms.items.services.ItemService;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.domain.VerificationReference;
import org.zafritech.zidingorms.database.repositories.VerificationReferenceRepository;
import org.zafritech.zidingorms.items.services.ArtifactService;

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
    private VerificationReferenceRepository vrRepository;
    
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
    
    @Autowired
    private ExcelFunctions excelFunctions;
    
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

            Workbook workbook = excelFunctions.getExcelWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);

            while(i <= worksheet.getLastRowNum()) {
//            while ((i <= 501) && (i <= worksheet.getLastRowNum())) {

                ItemDao itemDao = new ItemDao();
                Item item;
                Row row = worksheet.getRow(i++);

                // Item System ID
                itemDao.setSysId((String) excelFunctions.getExcelCellValue(row.getCell(0)));

                // Item Identifier
                String ident = (String) excelFunctions.getExcelCellValue(row.getCell(1));
                itemDao.setIdentifier((ident != null && !ident.isEmpty()) ? ident : "");

                // Is the Item a Requirement
                String itemClass = (String) excelFunctions.getExcelCellValue(row.getCell(2));
                itemDao.setItemClass((itemClass.equalsIgnoreCase("DEF")) ? ItemClass.REQUIREMENT.name() : itemClass.toUpperCase());

                // Item value
                String value = (String) excelFunctions.getExcelCellValue(row.getCell(3));
                itemDao.setItemValue((value != null && !value.isEmpty()) ? value : "");

                // Set the Requirement type
                itemDao.setItemType((itemClass.equalsIgnoreCase("DEF") || itemClass.equalsIgnoreCase("REQUIREMENT")) 
                        ? itemTypeRepository.findByItemTypeName("Functional")
                        : itemTypeRepository.findByItemTypeName("None"));
                
                // Item level in hierachy
                int level = (int) (double) excelFunctions.getExcelCellValue(row.getCell(4));
                itemDao.setItemLevel(level);

                if (row.getCell(5).getCellTypeEnum() == CellType.NUMERIC) {
                    
                    int itemIndex = (int) (double) excelFunctions.getExcelCellValue(row.getCell(5));
                    itemDao.setSortIndex(itemIndex);
                    index = itemIndex;
                    
                } else {
                    
                    itemDao.setSortIndex(index++);
                }

                itemDao.setArtifactId(artifact.getId());
                item = this.saveItemDao(itemDao);
                
                // Item comments
                String clientComment = (row.getCell(6, Row.RETURN_BLANK_AS_NULL) != null) 
                                     ? (String) excelFunctions.getExcelCellValue(row.getCell(6, Row.RETURN_BLANK_AS_NULL)) 
                                     : "";
                String contractorComment = (row.getCell(7, Row.RETURN_BLANK_AS_NULL) != null) 
                                     ? (String) excelFunctions.getExcelCellValue(row.getCell(7, Row.RETURN_BLANK_AS_NULL)) 
                                     : "";
                
                // Save client comment
                if (clientComment != null && !clientComment.isEmpty()) {
                    
                    ItemComment comment = new ItemComment(item, clientComment, userService.findByEmail("client@astad.qa"));
                    commentRepository.save(comment);
                    itemService.incrementCommentCount(item.getId());
                }
                
                // Save client comment
                if (contractorComment != null && !contractorComment.isEmpty()) {
                    
                    ItemComment comment = new ItemComment(item, contractorComment, userService.findByEmail("contractor@astad.qa"));
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
        styles = excelFunctions.getExcelStyles(workbook);
        
        workbook = addRequirementsToWorksheet(workbook, id);
        workbook = addCommentsToWorksheet(workbook, id);
        workbook = addLinksToWorksheet(workbook, id);
            
        return workbook;
    }
    
    private XSSFWorkbook addRequirementsToWorksheet(XSSFWorkbook workbook, Long id) {
        
        XSSFSheet reqSheet = workbook.createSheet("Requirements");
        
        // Column Widths
        reqSheet.setColumnWidth(0, 5000);       // SysId
        reqSheet.setColumnWidth(1, 5000);       // UniqueID (requirement)
        reqSheet.setColumnWidth(2, 3000);       // Lead (Lead department)
        reqSheet.setColumnWidth(3, 5000);       // ItemType (HEADER/PROSE/REQUIREMENT)
        reqSheet.setColumnWidth(4, 25000);      // Value (TEXT)
        reqSheet.setColumnWidth(5, 4000);       // Status
        reqSheet.setColumnWidth(6, 7000);       // VV_Method
        reqSheet.setColumnWidth(7, 10000);      // VV_Reference
        reqSheet.setColumnWidth(8, 10000);      // VV_Evidence
        reqSheet.setColumnWidth(9, 15000);      // ClientComment
        reqSheet.setColumnWidth(10, 15000);     // ContractorComment
        reqSheet.setColumnWidth(11, 3000);      // Level
        reqSheet.setColumnWidth(12, 3000);      // SortIndex
        reqSheet.setColumnWidth(13, 3000);      // Confirmation Request
          
        Row headerRow = reqSheet.createRow(0);
        headerRow.setHeightInPoints(28);
        
        Cell cellSysId = headerRow.createCell(0);
        cellSysId.setCellValue("SYS_ID");
        cellSysId.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellIdent = headerRow.createCell(1);
        cellIdent.setCellValue("UNIQUE_ID");
        cellIdent.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellLead = headerRow.createCell(2);
        cellLead.setCellValue("LEAD");
        cellLead.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellClass = headerRow.createCell(3);
        cellClass.setCellValue("TYPE");
        cellClass.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellValue = headerRow.createCell(4);
        cellValue.setCellValue("VALUE");
        cellValue.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellStatus = headerRow.createCell(5);
        cellStatus.setCellValue("STATUS");
        cellStatus.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellVVMethod = headerRow.createCell(6);
        cellVVMethod.setCellValue("VV_METHOD");
        cellVVMethod.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellVVRef = headerRow.createCell(7);
        cellVVRef.setCellValue("VV_REF");
        cellVVRef.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellVVEvidence = headerRow.createCell(8);
        cellVVEvidence.setCellValue("VV_EVIDENCE");
        cellVVEvidence.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellClientComment = headerRow.createCell(9);
        cellClientComment.setCellValue("CLIENT_COMMENT");
        cellClientComment.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellContractorComment = headerRow.createCell(10);
        cellContractorComment.setCellValue("CONTRACTOR_COMMENT");
        cellContractorComment.setCellStyle((CellStyle) styles.get("HeaderLeftAlign")); 
        
        Cell cellLevel = headerRow.createCell(11);
        cellLevel.setCellValue("LEVEL");
        cellLevel.setCellStyle((CellStyle) styles.get("HeaderCenterAlign"));
        
        Cell cellSortIdx = headerRow.createCell(12);
        cellSortIdx.setCellValue("INDEX");
        cellSortIdx.setCellStyle((CellStyle) styles.get("HeaderCenterAlign"));
        
        Cell cellRequest = headerRow.createCell(13);
        cellRequest.setCellValue("REQUEST");
        cellRequest.setCellStyle((CellStyle) styles.get("HeaderCenterAlign"));
        
        List<Item> items = itemRepository.findByArtifactId(id);
        
        int rowCount = 0;
        
        for (Item item : items ) {
                    
            Row row = reqSheet.createRow(++rowCount);
            
            String method = "";
            String reference = "";
            String evidence = "";
            String status = "";
            
            VerificationReference verification = vrRepository.findFirstByItem(item);
            
            if (verification != null) {
                
                method = (verification.getMethod() != null) ? verification.getMethod().getMethodCode() : "";
                reference = (verification.getVvReferences() != null) ? verification.getVvReferences() : "";
                evidence = (verification.getVvEvidence() != null) ? verification.getVvEvidence() : "";
                status = (verification.getVvStatus() != null) ? verification.getVvStatus().name() : "";
            }
            
            Cell cell0 = row.createCell(0);
            if (item.getSysId() instanceof String) { cell0.setCellValue(item.getSysId()); } 
            cell0.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
            
            Cell cell1 = row.createCell(1);
            if (item.getIdentifier() instanceof String) { cell1.setCellValue(item.getIdentifier()); } 
            cell1.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
            
            Cell cell2 = row.createCell(2);
            if (item.getItemCategory() != null) {
                if (item.getItemCategory().getCategoryCode() instanceof String) { 
                    cell2.setCellValue(item.getItemCategory().getCategoryCode()); 
                } else { cell2.setCellValue(""); }
            } else { cell2.setCellValue(""); }
            cell2.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
            
            Cell cell3 = row.createCell(3);
            if (item.getItemClass() instanceof String) { cell3.setCellValue(item.getItemClass()); } 
            cell3.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
            
            Cell cell4 = row.createCell(4);
            if (item.getItemValue() instanceof String) { cell4.setCellValue(item.getItemValue()); } 
            cell4.setCellStyle((CellStyle) styles.get("BodyLeftAlignWrapText"));
            
            Cell cell5 = row.createCell(5);
            if (item.getItemStatus() != null) {
                if (item.getItemStatus().name() instanceof String) { 
                    cell5.setCellValue(item.getItemStatus().name()); 
                } else { cell5.setCellValue(""); }
            } else { cell5.setCellValue(""); }
            cell5.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
                    
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(method);
            cell6.setCellStyle((CellStyle) styles.get("BodyLeftAlign"));
                    
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(reference);
            cell7.setCellStyle((CellStyle) styles.get("BodyLeftAlignWrapText"));
                    
            Cell cell8 = row.createCell(8);
            cell8.setCellValue(evidence);
            cell8.setCellStyle((CellStyle) styles.get("BodyLeftAlignWrapText"));
                    
            Cell cell9 = row.createCell(9);
            cell9.setCellValue(getUserItemComment("client@astad.qa", item.getId()));
            cell9.setCellStyle((CellStyle) styles.get("BodyLeftAlignWrapText"));
                    
            Cell cell10 = row.createCell(10);
            cell10.setCellValue(getUserItemComment("contractor@astad.qa", item.getId()));
            cell10.setCellStyle((CellStyle) styles.get("BodyLeftAlignWrapText"));
                    
            Cell cell11 = row.createCell(11);
            cell11.setCellValue(item.getItemLevel());
            cell11.setCellStyle((CellStyle) styles.get("BodyCenterAlign"));
            
            Cell cell12 = row.createCell(12);
            cell12.setCellValue(item.getSortIndex());
            cell12.setCellStyle((CellStyle) styles.get("BodyCenterAlign"));
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
        linkSheet.setColumnWidth(2, 6000);   // Src
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
    
    private String getUserItemComment(String email, Long id) {
        
        ItemComment comment = commentRepository.findFirstByAuthorAndItemIdOrderByCreationDateDesc(userService.findByEmail(email), id);
            
        return (comment != null) ? comment.getComment() : "";
    }
}