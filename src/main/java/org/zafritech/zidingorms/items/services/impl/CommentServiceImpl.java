/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.items.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.dao.CommentDao;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.ItemComment;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.database.repositories.ItemRepository;
import org.zafritech.zidingorms.io.excel.ExcelFunctions;
import org.zafritech.zidingorms.items.services.CommentService;
import org.zafritech.zidingorms.items.services.ItemService;

/**
 *
 * @author LukeS
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private ExcelFunctions excelFunctions;
    
    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;
    
    @Autowired
    private ItemCommentRepository commentRepository;
    
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ItemComment saveComment(Item item, String comment, User user) {

        ItemComment newComment = new ItemComment(item, comment, user);
        
        ItemComment savedComment = commentRepository.save(newComment);
        itemService.incrementCommentCount(item.getId());
        
        return savedComment;
    }

    @Override
    public ItemComment saveCommentDao(CommentDao commentDao) {

        Item item = itemRepository.findOne(commentDao.getItemId());
        ItemComment comment = new ItemComment(item, 
                                              commentDao.getComment(), 
                                              userService.loggedInUser());
        
        ItemComment savedComment = commentRepository.save(comment);
        itemService.incrementCommentCount(item.getId());
        
        return savedComment;
    }

    @Override
    public List<ItemComment> findByItemId(Long id) {

        return commentRepository.findByItemId(id);
    }

    @Override
    public List<ItemComment> findByItemIdOrderByCreationDateDesc(Long id) {

        return commentRepository.findByItemIdOrderByCreationDateDesc(id);
    }

    @Override
    public Integer refreshItemComments(String filePath) {

        List<ItemComment> comments = commentRepository.findAll();
        
        if (comments != null) {
            
            for (ItemComment comment : comments) {
                
                Item item = comment.getItem();
                item.setCommentCount(0);
                itemRepository.save(item);
                
                commentRepository.delete(comment); 
            }
        }
                
        try {
            
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            
            Workbook workbook = excelFunctions.getExcelWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            User client = userService.findByEmail("client@astad.qa");
            User contractor = userService.findByEmail("contractor@astad.qa");
            
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                
                String clientComment = (String) excelFunctions.getExcelCellValue(row.getCell(11));
                String contractorComment = (String) excelFunctions.getExcelCellValue(row.getCell(12));
                
                if ((clientComment != null && !clientComment.isEmpty()) 
                || ((contractorComment != null && !contractorComment.isEmpty()))) {
                    
                    String sysId = (String) excelFunctions.getExcelCellValue(row.getCell(0));
                    Item item = itemRepository.findBySysId(sysId);
                    
                    if (item != null) {
                    
                        if (clientComment != null && !clientComment.isEmpty()) {

                            ItemComment comment = new ItemComment(item, clientComment, client);
                            item.setCommentCount(item.getCommentCount() + 1); 

                            commentRepository.save(comment);
                            itemRepository.save(item);
                        }

                        if (contractorComment != null && !contractorComment.isEmpty()) {

                            ItemComment comment = new ItemComment(item, contractorComment, contractor);
                            item.setCommentCount(item.getCommentCount() + 1); 

                            commentRepository.save(comment);
                            itemRepository.save(item);
                        }
                        
                    } else {
                        
                        System.out.println("Item with SYS_ID " + sysId + " does not exist in the database.");
                    }
                }
            }
            
        } catch (IOException e) {

            System.out.println(e.getMessage());
        }
        
        return commentRepository.findAll().size();
    }

    @Override
    public ItemComment getLastUserComment(User user, Long id) {

        ItemComment comment = commentRepository.findFirstByAuthorAndItemIdOrderByCreationDateDesc(user, id);
        
        return comment;
    }
}
