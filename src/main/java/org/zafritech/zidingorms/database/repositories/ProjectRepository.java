/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.database.domain.Project;

/**
 *
 * @author LukeS
 */
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByUuId(String uuid);
}
