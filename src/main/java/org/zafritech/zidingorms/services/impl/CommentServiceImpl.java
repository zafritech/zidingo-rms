/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.daos.CommentDao;
import org.zafritech.zidingorms.domain.ItemComment;
import org.zafritech.zidingorms.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.services.CommentService;
import org.zafritech.zidingorms.services.GeneralService;

/**
 *
 * @author LukeS
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private GeneralService generalService;
    
    @Autowired
    private ItemCommentRepository commentRepository;
    
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ItemComment saveCommentDao(CommentDao commentDao) {

        ItemComment comment = new ItemComment(itemRepository.findOne(commentDao.getItemId()), 
                                              commentDao.getComment(), 
                                              generalService.loggedInUser());
        
        return commentRepository.save(comment);
    }
}
