/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.commons.enums.ItemStatus;
import org.zafritech.zidingorms.core.commons.enums.TaskAction;
import org.zafritech.zidingorms.database.dao.MsgDao;
import org.zafritech.zidingorms.database.dao.TaskDao;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.ItemComment;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.ItemCategoryRepository;
import org.zafritech.zidingorms.database.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.database.repositories.ItemRepository;
import org.zafritech.zidingorms.database.repositories.TaskRepository;
import org.zafritech.zidingorms.database.repositories.UserRepository;
import org.zafritech.zidingorms.core.messages.MessageService;
import org.zafritech.zidingorms.database.domain.VerificationMethod;
import org.zafritech.zidingorms.database.domain.VerificationReference;
import org.zafritech.zidingorms.database.repositories.VerificationMethodRepository;
import org.zafritech.zidingorms.database.repositories.VerificationReferenceRepository;
import org.zafritech.zidingorms.io.excel.ExcelFunctions;
import org.zafritech.zidingorms.items.services.TaskService;

/**
 *
 * @author LukeS
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private TaskService taskService;
    
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
    
    @Autowired
    private ExcelFunctions excelFunctions;
    
    @Autowired
    private VerificationMethodRepository vvMethodRepository;
    
    @Autowired
    private VerificationReferenceRepository vvReferenceRepository;
            
    @Override
    public boolean loadPMSLeads(String filePath) {
        
        try {
            
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            
            Workbook workbook = excelFunctions.getExcelWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                String lead = (String) excelFunctions.getExcelCellValue(row.getCell(8));
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
                    
                    String id = (String) excelFunctions.getExcelCellValue(row.getCell(0));
                    
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
            
            Workbook workbook = excelFunctions.getExcelWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                String lead = (String) excelFunctions.getExcelCellValue(row.getCell(8));
                
                if (lead != null && !lead.isEmpty()) {
                    
                    String id = (String) excelFunctions.getExcelCellValue(row.getCell(0));
                    
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
            
            Workbook workbook = excelFunctions.getExcelWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                String status = (String) excelFunctions.getExcelCellValue(row.getCell(6));
                
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
                            
                    String id = (String) excelFunctions.getExcelCellValue(row.getCell(0));
                    
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
            
            Workbook workbook = excelFunctions.getExcelWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            String batchId = UUID.randomUUID().toString();
            
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                String confRequest = (String) excelFunctions.getExcelCellValue(row.getCell(12));
                
                if (confRequest != null && !confRequest.isEmpty() && confRequest.equals("Yes")) {
                    
                    TaskAction action = TaskAction.NONE; 
                    String sysId = (String) excelFunctions.getExcelCellValue(row.getCell(0));
                    String status = (String) excelFunctions.getExcelCellValue(row.getCell(5));
                    String clientComment = (String) excelFunctions.getExcelCellValue(row.getCell(9));
                    String contractorComment = (String) excelFunctions.getExcelCellValue(row.getCell(10));
                    
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

                        taskService.createTask(task);

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
    
    @Override
    public boolean updateVandVMethods(String filePath) {
        
        try {
           
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            
            Workbook workbook = excelFunctions.getExcelWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                
                String sysId = (String) excelFunctions.getExcelCellValue(row.getCell(0));
                String method = (String) excelFunctions.getExcelCellValue(row.getCell(7));
                String refs = (String) excelFunctions.getExcelCellValue(row.getCell(8));
                String edidence = (String) excelFunctions.getExcelCellValue(row.getCell(10));
                
                if (sysId != null && !sysId.isEmpty()) {
                    
                    Item item = itemRepository.findBySysId(sysId);
                    
                    if (item != null) {
                        
                        if (method != null && !method.isEmpty()) {
                            
                            VerificationMethod vvMethod = vvMethodRepository.findByMethodCode(method);
                            
                            // Check if item already exists in Verification References
                            VerificationReference vvReference = vvReferenceRepository.findFirstByItem(item);
                            
                            if (vvReference == null) {
                            
                                vvReference = new VerificationReference(item, vvMethod);
                                
                            } else {
                                
                                vvReference.setMethod(vvMethod); 
                            }
                            
                            if (refs != null && !refs.isEmpty()) {
                                
                                vvReference.setVvReferences(refs);
                            }
                            
                            if (edidence != null && !edidence.isEmpty()) {
                                
                                vvReference.setVvEvidence(edidence); 
                            }
                            
                            vvReferenceRepository.save(vvReference); 
                        }
                        
                    } else {
                        
                        System.out.println("Item with SYS_ID " + sysId + " does not exist in the database.");
                    }
                }
            }
            
            return true;
            
        } catch (IOException e) {

            System.out.println(e.getMessage());
        }
        
        return false;
    }
}
