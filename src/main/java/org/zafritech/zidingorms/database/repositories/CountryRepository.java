/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.database.domain.Country;

/**
 *
 * @author LukeS
 */
public interface CountryRepository extends CrudRepository<Country, Long> {
    
    @Override
    List<Country> findAll();
}
