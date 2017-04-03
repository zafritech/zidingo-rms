/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.daos.CommentDao;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemComment;
import org.zafritech.zidingorms.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.services.CommentService;
import org.zafritech.zidingorms.services.GeneralService;
import org.zafritech.zidingorms.services.ItemService;

/**
 *
 * @author LukeS
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private GeneralService generalService;

    @Autowired
    private ItemService itemService;
    
    @Autowired
    private ItemCommentRepository commentRepository;
    
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ItemComment saveCommentDao(CommentDao commentDao) {

        Item item = itemRepository.findOne(commentDao.getItemId());
        ItemComment comment = new ItemComment(item, 
                                              commentDao.getComment(), 
                                              generalService.loggedInUser());
        
        ItemComment savedComment = commentRepository.save(comment);
        itemService.incrementCommentCount(item.getId());
        
        return savedComment;
    }

    @Override
    public List<ItemComment> findByItemId(Long id) {

        return commentRepository.findByItemId(id);
    }
}
