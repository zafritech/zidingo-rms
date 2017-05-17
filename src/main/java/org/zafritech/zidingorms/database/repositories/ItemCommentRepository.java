/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.database.domain.ItemComment;
import org.zafritech.zidingorms.database.domain.User;

/**
 *
 * @author LukeS
 */
public interface ItemCommentRepository extends CrudRepository<ItemComment, Long> {

    @Override
    List<ItemComment> findAll();
    
    List<ItemComment> findByItemId(Long id);
    
    List<ItemComment> findByItemIdOrderByCreationDateDesc(Long id);
    
    ItemComment findFirstByAuthorAndItemIdOrderByCreationDateDesc(User user, Long id);
}
