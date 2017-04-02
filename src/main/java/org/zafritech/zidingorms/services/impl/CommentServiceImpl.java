/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.daos.CommentDao;
import org.zafritech.zidingorms.daos.converters.DaoToCommentConverter;
import org.zafritech.zidingorms.domain.ItemComment;
import org.zafritech.zidingorms.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.services.CommentService;

/**
 *
 * @author LukeS
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    ItemCommentRepository commentRepository;
    
    @Override
    public ItemComment saveCommentDao(CommentDao commentDao) {
        
        DaoToCommentConverter converter = new DaoToCommentConverter();

        return commentRepository.save(converter.convert(commentDao)); 
    }
}
