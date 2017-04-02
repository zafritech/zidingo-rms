package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity(name = "TBL_ROLES")
public class Role implements Serializable {

	private static final long serialVersionUID = -8471742749314769250L;

	@Id
    @GeneratedValue
    private Long Id;
    
    private String uuId;
    
    private String roleName;

    @ManyToMany(mappedBy = "userRoles")
    private Set<User> users = new HashSet<User>();
    
    @Override
    public String toString() {
        return "User Role{"
                + "ID:" + getId()
                + ", Role Name = '" + getRoleName() + '\''
                + '}';
    }

    public Role() {}
    
    public Role(String name){
        
        this.uuId = UUID.randomUUID().toString();
        this.roleName = name;
    }

    /**
     * @return the Id
     */
    public Long getId() {
        return Id;
    }

    /**
     * @return the uuId
     */
    public String getUuId() {
        return uuId;
    }

    /**
     * @param uuId the uuId to set
     */
    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return the users
     */
    public Set<User> getUsers() {
        return users;
    }
    
    /**
     * @param users the users to set
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}