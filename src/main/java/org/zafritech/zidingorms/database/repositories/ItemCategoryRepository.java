/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.database.domain.User;

/**
 *
 * @author LukeS
 */
public interface ItemCategoryRepository extends PagingAndSortingRepository<ItemCategory, Long> {
    
    ItemCategory findByUuId(String uuid);
    
    List<ItemCategory> findByProjectOrderByCategoryNameAsc(Project project);
    
    List<ItemCategory> findByProjectOrderByCategoryNameAsc(Pageable pageable, Project project);
    
    ItemCategory findFirstByCategoryCode(String code);
    
    List<ItemCategory> findByLead(User user);
}
