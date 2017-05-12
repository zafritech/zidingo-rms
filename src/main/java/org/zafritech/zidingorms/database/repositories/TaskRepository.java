/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.database.domain.Task;
import org.zafritech.zidingorms.database.domain.User;

/**
 *
 * @author LukeS
 */
public interface TaskRepository extends CrudRepository<Task, Long> {
    
    List<Task> findByAssignedToAndCompleted(User user, boolean completed);
    
    List<Task> findByBatchId(String batchId);
    
    List<Task> findByAssignedTo(User user);
}
