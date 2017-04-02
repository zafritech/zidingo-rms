/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.zafritech.zidingorms.daos.CommentDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zafritech.zidingorms.domain.ItemComment;
import org.zafritech.zidingorms.services.CommentService;
import org.zafritech.zidingorms.services.impl.CommentServiceImpl;

/**
 *
 * @author LukeS
 */
@RestController
public class CommentController {
    
    private CommentService commentService;
    
    @Autowired
    public void setCommentService(CommentServiceImpl commentService) {

        this.commentService = commentService;
    }
    
    @RequestMapping(value = "/api/comments/new", method = RequestMethod.POST)
    public ResponseEntity<ItemComment> newComment(@RequestBody CommentDao commentDao) {
        
        ItemComment comment = commentService.saveCommentDao(commentDao);

        return new ResponseEntity<ItemComment>(comment, HttpStatus.OK);
    }
}
