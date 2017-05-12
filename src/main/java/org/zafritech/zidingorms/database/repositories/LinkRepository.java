/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.Link;

/**
 *
 * @author LukeS
 */
public interface LinkRepository extends CrudRepository<Link, Long> {
    
    List<Link> findBySrcItem(Item item);
    
    List<Link> findByDstItem(Item item);
}
