package com.example.vidupcoremodule.core.entity.DatabaseEntities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "fname")
    String fName;

    @Column(name = "lname")
    String lName;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "age")
    Integer age;

    @Column(name = "sex")
    String sex;

    @Column(name = "contact")
    String contact;

    @Column(name = "username", unique = true)
    String userName;

    @JsonIgnore
    @Column(name = "password")
    String password;

    @JsonIgnore
    @Column(name = "profile_image", nullable = true)
    byte[] profileImage;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public Integer getId() {
        return this.id;
    }

    public String getFName() {
        return this.fName;
    }

    public String getLName() {
        return this.lName;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getAge() {
        return this.age;
    }

    public String getSex() {
        return this.sex;
    }

    public String getContact() {
        return this.contact;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public byte[] getProfileImage() {
        return this.profileImage;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof User other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (!Objects.equals(this$id, other$id)) return false;
        final Object this$fName = this.getFName();
        final Object other$fName = other.getFName();
        if (!Objects.equals(this$fName, other$fName)) return false;
        final Object this$lName = this.getLName();
        final Object other$lName = other.getLName();
        if (!Objects.equals(this$lName, other$lName)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (!Objects.equals(this$email, other$email)) return false;
        final Object this$age = this.getAge();
        final Object other$age = other.getAge();
        if (!Objects.equals(this$age, other$age)) return false;
        final Object this$sex = this.getSex();
        final Object other$sex = other.getSex();
        if (!Objects.equals(this$sex, other$sex)) return false;
        final Object this$contact = this.getContact();
        final Object other$contact = other.getContact();
        if (!Objects.equals(this$contact, other$contact)) return false;
        final Object this$userName = this.getUserName();
        final Object other$userName = other.getUserName();
        if (!Objects.equals(this$userName, other$userName)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (!Objects.equals(this$password, other$password)) return false;
        if (!java.util.Arrays.equals(this.getProfileImage(), other.getProfileImage())) return false;
        final Object this$roles = this.getRoles();
        final Object other$roles = other.getRoles();
        return Objects.equals(this$roles, other$roles);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $fName = this.getFName();
        result = result * PRIME + ($fName == null ? 43 : $fName.hashCode());
        final Object $lName = this.getLName();
        result = result * PRIME + ($lName == null ? 43 : $lName.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $age = this.getAge();
        result = result * PRIME + ($age == null ? 43 : $age.hashCode());
        final Object $sex = this.getSex();
        result = result * PRIME + ($sex == null ? 43 : $sex.hashCode());
        final Object $contact = this.getContact();
        result = result * PRIME + ($contact == null ? 43 : $contact.hashCode());
        final Object $userName = this.getUserName();
        result = result * PRIME + ($userName == null ? 43 : $userName.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        result = result * PRIME + java.util.Arrays.hashCode(this.getProfileImage());
        final Object $roles = this.getRoles();
        result = result * PRIME + ($roles == null ? 43 : $roles.hashCode());
        return result;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", fName=" + this.getFName() + ", lName=" + this.getLName() + ", email=" + this.getEmail() + ", age=" + this.getAge() + ", sex=" + this.getSex() + ", contact=" + this.getContact() + ", userName=" + this.getUserName() + ", password=" + "this.getPassword()" + ", roles=" + this.getRoles() + ")";
    }
}
