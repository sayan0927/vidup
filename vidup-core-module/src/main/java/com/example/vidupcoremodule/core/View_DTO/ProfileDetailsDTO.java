package com.example.vidupcoremodule.core.View_DTO;


import lombok.Data;

@Data
public class ProfileDetailsDTO {

    String fName;

    String lName;

    String email;

    Integer age;

    String sex;

    String contact;

    String userName;

    String password;

    String passwordConfirm;

    public boolean emailEmpty()
    {
        return email == null || email.isEmpty();
    }

}
