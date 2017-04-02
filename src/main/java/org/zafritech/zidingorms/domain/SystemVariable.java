package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "TBL_SYSTEM_VARIABLES")
public class SystemVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String uuId;

    private String variableName;

    private String variableValue;

    private String ownerType;

    private Long ownerId;

    public SystemVariable() {

    }

    public SystemVariable(String variableName,
            String variableValue,
            String ownerType,
            Long ownerId) {

        this.uuId = UUID.randomUUID().toString();
        this.variableName = variableName;
        this.variableValue = variableValue;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(String variableValue) {
        this.variableValue = variableValue;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
