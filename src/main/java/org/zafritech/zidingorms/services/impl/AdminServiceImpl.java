/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.commons.enums.ItemStatus;
import org.zafritech.zidingorms.commons.enums.TaskAction;
import org.zafritech.zidingorms.dao.MsgDao;
import org.zafritech.zidingorms.dao.TaskDao;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemCategory;
import org.zafritech.zidingorms.domain.ItemComment;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.ItemCategoryRepository;
import org.zafritech.zidingorms.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.TaskRepository;
import org.zafritech.zidingorms.repositories.UserRepository;
import org.zafritech.zidingorms.services.AdminService;
import org.zafritech.zidingorms.services.GeneralService;
import org.zafritech.zidingorms.services.MessageService;

/**
 *
 * @author LukeS
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private GeneralService generalService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private ItemCommentRepository itemCommentRepository;
    
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private MessageService messageService;
    
    @Override
    public boolean loadPMSLeads(String filePath) {
        
        try {
            
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            
            Workbook workbook = getWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                String lead = (String) getCellValue(row.getCell(8));
                String newLead;
                
                if (lead != null && !lead.isEmpty()) {
                    
                    switch (lead) {
                        case "PM":
                            newLead = "PJM";
                            break;
                        case "COM_Radio":
                            newLead = "RAD";
                            break;
                        case "QF":
                            newLead = "QFN";
                            break;
                        default:
                            newLead = lead;
                            break;
                    }
                    
                    String id = (String) getCellValue(row.getCell(0));
                    
                    ItemCategory cat = itemCategoryRepository.findFirstByCategoryCode(newLead);
                    
                    if (cat != null) {
                        
                        Item item = itemRepository.findBySysId(id);

                        if (item != null) {

                            item.setItemCategory(cat);
                            itemRepository.save(item);
                        }
                    }
                }
            }
            
        } catch (IOException e) {

        }
        
        return false;
    }
 
    @Override
    public boolean updateASTADLeads(String filePath) {
        
        try {
            
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            
            Workbook workbook = getWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                String lead = (String) getCellValue(row.getCell(8));
                
                if (lead != null && !lead.isEmpty()) {
                    
                    String id = (String) getCellValue(row.getCell(0));
                    
                    ItemCategory cat = itemCategoryRepository.findFirstByCategoryCode(lead);
                    
                    if (cat != null) {
                        
                        Item item = itemRepository.findFirstByIdentifier(id);

                        if (item != null) {

                            item.setItemCategory(cat);
                            itemRepository.save(item);
                        }
                    }
                }
            }
            
        } catch (IOException e) {

        }
        
        return false;
    }

    @Override
    public boolean updateItemStatuses(String filePath) {
        
        try {
            
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            
            Workbook workbook = getWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                String status = (String) getCellValue(row.getCell(6));
                
                if (status != null && !status.isEmpty()) {
                    
                    ItemStatus itemStatus = ItemStatus.NONE;
                    
                    switch (status) {
                        case "Closed":
                            itemStatus = ItemStatus.CLOSED;
                            break;
                        case "Confirmed":
                            itemStatus = ItemStatus.CONFIRMED;
                            break;
                        case "Defined":
                            itemStatus = ItemStatus.DEFINED;
                            break;
                        case "Open":
                            itemStatus = ItemStatus.OPEN;
                            break;
                        case "Selected":
                            itemStatus = ItemStatus.SELECTED;
                            break;
                        default:
                            itemStatus = ItemStatus.NONE;
                            break;
                    }
                            
                    String id = (String) getCellValue(row.getCell(0));
                    
                    Item item = itemRepository.findBySysId(id);
                    
                    if (item != null) {
                        
                        item.setItemStatus(itemStatus);
                        itemRepository.save(item);
                    }
                }
            }
            
        } catch (IOException e) {

        }
        
        return false;
    }
    
    @Override
    public boolean updateConfirmationRequests(String filePath) {

        try {
            
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            
            Workbook workbook = getWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            String batchId = UUID.randomUUID().toString();
            
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                String confRequest = (String) getCellValue(row.getCell(12));
                
                if (confRequest != null && !confRequest.isEmpty() && confRequest.equals("Yes")) {
                    
                    TaskAction action = TaskAction.NONE; 
                    String sysId = (String) getCellValue(row.getCell(0));
                    String status = (String) getCellValue(row.getCell(5));
                    String clientComment = (String) getCellValue(row.getCell(9));
                    String contractorComment = (String) getCellValue(row.getCell(10));
                    
                    if (null == status) {
                        
                        action = TaskAction.NONE;
                        
                    } else switch (status) {
                        
                        case "Confirmed":
                        case "Cosed":
                            action = TaskAction.ACKNOWLEDGE_REQ;
                            break;
                            
                        default:
                            action = TaskAction.CONFIRM_REQ;
                            break;
                    }
                    
                    Item item = itemRepository.findBySysId(sysId);
                    
                    if (item != null) {
                    
                        Long itemId = item.getId();

                        // Create new Task
                        TaskDao task = new TaskDao();
                        task.setItemId(itemId); 
                        task.setTaskAction(action); 
                        task.setBatchId(batchId); 

                        generalService.createTask(task);

                        // Create comments
                        User client = userRepository.findByEmail("client@astad.qa");
                        User contractor = userRepository.findByEmail("contractor@astad.qa");

                        if (clientComment != null) {

                            ItemComment comment1 = new ItemComment(itemRepository.findBySysId(sysId), clientComment, client);
                            itemCommentRepository.save(comment1);
                        }

                        if (contractorComment != null) {

                            ItemComment comment2 = new ItemComment(itemRepository.findBySysId(sysId), contractorComment, contractor);
                            itemCommentRepository.save(comment2);
                        }
                        
                    } else {
                        
                        System.out.println("Item with SYS_ID " + sysId + " does not exist in the database.");
                    }
                }
            }
            
            // Send notofication
            Integer tasksCount = taskRepository.findByBatchId(batchId).size();
            String msg = tasksCount + " new request confirmations have been received. " +
                         "Please check your task list for confirmations assigned to you.";

            MsgDao msgDao = new MsgDao();

            msgDao.setSender(userRepository.findByEmail("admin@zafritech.org").getFirstName());
            msgDao.setSubject("New Request Confirmations");
            msgDao.setMessage(msg);

            messageService.sendMessage(msgDao);
            
        } catch (IOException e) {

        }

        return false;
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
}
