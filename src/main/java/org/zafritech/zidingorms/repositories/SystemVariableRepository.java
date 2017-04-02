package org.zafritech.zidingorms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.domain.SystemVariable;

public interface SystemVariableRepository extends CrudRepository<SystemVariable, Long> {

    List<SystemVariable> findByOwnerIdAndOwnerTypeAndVariableName(Long id, String ownerType, String name);

    List<SystemVariable> findByOwnerIdAndOwnerTypeAndVariableNameOrderByVariableValue(Long id, String ownerType, String name);
}
