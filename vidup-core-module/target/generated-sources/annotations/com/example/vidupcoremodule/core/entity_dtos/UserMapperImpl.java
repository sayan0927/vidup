package com.example.vidupcoremodule.core.entity_dtos;

import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.dtos.UserDTO;
import java.util.Arrays;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T12:15:07+0530",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setFName( user.getFName() );
        userDTO.setLName( user.getLName() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setAge( user.getAge() );
        userDTO.setSex( user.getSex() );
        userDTO.setContact( user.getContact() );
        userDTO.setUserName( user.getUserName() );
        byte[] profileImage = user.getProfileImage();
        if ( profileImage != null ) {
            userDTO.setProfileImage( Arrays.copyOf( profileImage, profileImage.length ) );
        }

        return userDTO;
    }
}
