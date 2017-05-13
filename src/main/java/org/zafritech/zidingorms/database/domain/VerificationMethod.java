package org.zafritech.zidingorms.database.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity(name = "TBL_VERIFICATION_METHODS")
public class VerificationMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String uuId;

    @NotNull
    private String methodCode;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String methodName;

    @Column(columnDefinition = "TEXT")
    private String methodDescription;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public VerificationMethod() {
        
    }
    
    public VerificationMethod(String methodCode, String methodName) {
        
        this.uuId = UUID.randomUUID().toString();
        this.methodCode = methodCode;
        this.methodName = methodName;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public VerificationMethod(String methodCode, String methodName, String methodDescription) {
        
        this.uuId = UUID.randomUUID().toString();
        this.methodCode = methodCode;
        this.methodName = methodName;
        this.methodDescription = methodDescription;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDescription() {
        return methodDescription;
    }

    public void setMethodDescription(String methodDescription) {
        this.methodDescription = methodDescription;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
