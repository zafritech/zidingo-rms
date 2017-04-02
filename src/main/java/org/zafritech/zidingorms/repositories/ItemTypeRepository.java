/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.domain.ItemType;

/**
 *
 * @author LukeS
 */
public interface ItemTypeRepository extends CrudRepository<ItemType, Long> {

    ItemType findByItemTypeName(String name);

    @Override
    List<ItemType> findAll();
}
