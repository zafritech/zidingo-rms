/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemCategory;
import org.zafritech.zidingorms.domain.Project;

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
