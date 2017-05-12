/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.database.domain.Role;

/**
 *
 * @author LukeS
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByRoleName(String string);

}
