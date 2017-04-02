/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.domain.Item;

/**
 *
 * @author LukeS
 */
public interface ItemRepository extends CrudRepository<Item, Long> {
    
	Item findBySysId(String sysId);
	
	List<Item> findByArtifactId(Long id);
	
	Item findFirstByOrderBySysIdDesc();
	
	Item findFirstByIdentifierContainingOrderByIdentifierDesc(String identTemplate); 
	
	List<Item> findByArtifactIdOrderBySortIndexAsc(Long id);
}
