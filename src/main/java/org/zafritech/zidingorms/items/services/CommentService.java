/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.items.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.database.dao.CommentDao;
import org.zafritech.zidingorms.database.domain.ItemComment;

/**
 *
 * @author LukeS
 */
@Service
public interface CommentService {

    ItemComment saveCommentDao(CommentDao commentDao);
    
    List<ItemComment> findByItemId(Long id);
    
    List<ItemComment> findByItemIdOrderByCreationDateDesc(Long id);
    
    Integer refreshItemComments(String filePath);
}
