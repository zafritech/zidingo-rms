/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.zafritech.zidingorms.database.dao.CommentDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.zafritech.zidingorms.database.domain.ItemComment;
import org.zafritech.zidingorms.items.services.CommentService;
import org.zafritech.zidingorms.items.services.impl.CommentServiceImpl;

/**
 *
 * @author LukeS
 */
@RestController
public class CommentRestController {

    @Value("${zidingo.upload-dir}")
    private String upload_dir;
    
    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentServiceImpl commentService) {

        this.commentService = commentService;
    }

    @RequestMapping(value = "/api/comments/new", method = RequestMethod.POST)
    public ResponseEntity<Long> newComment(@RequestBody CommentDao commentDao) {

        ItemComment comment = commentService.saveCommentDao(commentDao);

        return new ResponseEntity<Long>(comment.getId(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/itemcomments/{id}", method = RequestMethod.GET)
    public @ResponseBody List<ItemComment> getItemComments(@PathVariable(value = "id") Long id) {
        
        List<ItemComment> comments = commentService.findByItemIdOrderByCreationDateDesc(id);
        
        return comments;
    }
    
    @RequestMapping(value = "/api/itemcomments/refresh", method = RequestMethod.GET)
    public ResponseEntity<Integer> refreshItemComments() {
        
        Integer comments = commentService.refreshItemComments(upload_dir + "DOORS-v23.xlsx");
        
        return new ResponseEntity<Integer>(comments, HttpStatus.OK);
    }
}
