/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.items.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.dao.CommentDao;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.ItemComment;
import org.zafritech.zidingorms.database.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.database.repositories.ItemRepository;
import org.zafritech.zidingorms.items.services.CommentService;
import org.zafritech.zidingorms.items.services.ItemService;

/**
 *
 * @author LukeS
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private UserService userService;

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
}
