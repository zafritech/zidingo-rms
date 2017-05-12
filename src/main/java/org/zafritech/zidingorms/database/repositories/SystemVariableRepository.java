package org.zafritech.zidingorms.database.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.database.domain.SystemVariable;

public interface SystemVariableRepository extends CrudRepository<SystemVariable, Long> {

    List<SystemVariable> findByOwnerIdAndOwnerTypeAndVariableName(Long id, String ownerType, String name);

    List<SystemVariable> findByOwnerIdAndOwnerTypeAndVariableNameOrderByVariableValue(Long id, String ownerType, String name);
}
