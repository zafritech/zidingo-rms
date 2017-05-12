package org.zafritech.zidingorms.database.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    private String roleDisplayName;

    @ManyToMany(mappedBy = "userRoles")
    @JsonManagedReference
    private Set<User> users = new HashSet<User>();

    @Override
    public String toString() {
        return "User Role{"
                + "ID:" + getId()
                + ", Role Name = '" + getRoleName() + '\''
                + ", Role Display Name = '" + getRoleDisplayName() + '\''
                + '}';
    }

    public Role() {
    }

    public Role(String name) {

        this.uuId = UUID.randomUUID().toString();
        this.roleName = name;
    }

    public Role(String name, String displayName) {

        this.uuId = UUID.randomUUID().toString();
        this.roleName = name;
        this.roleDisplayName = displayName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return Id;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDisplayName() {
        return roleDisplayName;
    }

    public void setRoleDisplayName(String roleDisplayName) {
        this.roleDisplayName = roleDisplayName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
