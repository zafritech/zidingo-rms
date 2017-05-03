/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.domain.ItemCategory;
import org.zafritech.zidingorms.domain.Project;

/**
 *
 * @author LukeS
 */
public interface ItemCategoryRepository extends CrudRepository<ItemCategory, Long> {
    
    List<ItemCategory> findByProjectOrderByCategoryNameAsc(Project project);
}
