package org.zafritech.zidingorms.database.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity(name = "TBL_USERS")
public class User implements Serializable {

    private static final long serialVersionUID = -687205007426664632L;

    @Id
    @GeneratedValue
    private Long id;

    private String uuId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String userName;

    private String password;

    private String firstName;

    private String lastName;
    
    private String mainRole;
    
    private String phoneNumber;
    
    private String mobileNumber;
    
    private String address;
    
    private String country;
    
    private String state;
    
    private String city;
    
    private String postCode;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "XREF_USER_ROLES",
               joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @JsonBackReference
    private Set<Role> userRoles = new HashSet<Role>();

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Override
    public String toString() {
        
        return "User {"
                + "id:" + getId()
                + ", User Email = '" + getEmail() + '\''
                + '}';
    }

    public User() {
    }

    public User(String email, String password) {

        this.uuId = UUID.randomUUID().toString();
        this.email = email;
        this.userName = email;
        this.firstName = email.substring(0, 1).toUpperCase() + email.substring(1, email.indexOf('@')); 
        this.password = new BCryptPasswordEncoder().encode(password);
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public User(String email, String password, HashSet<Role> roles) {

        this.uuId = UUID.randomUUID().toString();
        this.email = email;
        this.userName = email;
        this.firstName = email.substring(0, 1).toUpperCase() + email.substring(1, email.indexOf('@')); 
        this.password = new BCryptPasswordEncoder().encode(password);
        this.userRoles = roles;
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public User(String email, String password, String firstName, String lastName, HashSet<Role> roles) {

        this.uuId = UUID.randomUUID().toString();
        this.email = email;
        this.userName = email;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRoles = roles;
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMainRole() {
        return mainRole;
    }

    public void setMainRole(String mainRole) {
        this.mainRole = mainRole;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
