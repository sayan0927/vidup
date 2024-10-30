package com.example.vidupcoremodule.dtos;

public class UserDTO {


    Integer id;


    String fName;


    String lName;


    String email;


    Integer age;


    String sex;


    String contact;

    String userName;

    byte[] profileImage;

    public UserDTO() {
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

    public byte[] getProfileImage() {
        return this.profileImage;
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

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UserDTO)) return false;
        final UserDTO other = (UserDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$fName = this.getFName();
        final Object other$fName = other.getFName();
        if (this$fName == null ? other$fName != null : !this$fName.equals(other$fName)) return false;
        final Object this$lName = this.getLName();
        final Object other$lName = other.getLName();
        if (this$lName == null ? other$lName != null : !this$lName.equals(other$lName)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$age = this.getAge();
        final Object other$age = other.getAge();
        if (this$age == null ? other$age != null : !this$age.equals(other$age)) return false;
        final Object this$sex = this.getSex();
        final Object other$sex = other.getSex();
        if (this$sex == null ? other$sex != null : !this$sex.equals(other$sex)) return false;
        final Object this$contact = this.getContact();
        final Object other$contact = other.getContact();
        if (this$contact == null ? other$contact != null : !this$contact.equals(other$contact)) return false;
        final Object this$userName = this.getUserName();
        final Object other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) return false;
        if (!java.util.Arrays.equals(this.getProfileImage(), other.getProfileImage())) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UserDTO;
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
        result = result * PRIME + java.util.Arrays.hashCode(this.getProfileImage());
        return result;
    }

    public String toString() {
        return "UserDTO(id=" + this.getId() + ", fName=" + this.getFName() + ", lName=" + this.getLName() + ", email=" + this.getEmail() + ", age=" + this.getAge() + ", sex=" + this.getSex() + ", contact=" + this.getContact() + ", userName=" + this.getUserName() +")";
    }
}
