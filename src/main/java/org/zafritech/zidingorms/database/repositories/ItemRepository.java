/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.Project;

/**
 *
 * @author LukeS
 */
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

    Item findBySysId(String sysId);
    
    Item findFirstByIdentifier(String ident);

    List<Item> findByArtifactId(Long id);

    Item findFirstByOrderBySysIdDesc();

    Item findFirstByIdentifierContainingOrderByIdentifierDesc(String identTemplate);

    List<Item> findByArtifactIdOrderBySortIndexAsc(Long id);
    
    List<Item> findByArtifact(Pageable pageable, Artifact artifact);
    
    List<Item> findByItemCategory(ItemCategory category);
    
    List<Item> findByItemCategory(Pageable pageable, ItemCategory category);
}
