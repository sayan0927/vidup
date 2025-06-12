package com.example.vidupcoremodule.core.entity_dtos;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {


  UserMapper INSTANCE =   Mappers.getMapper(UserMapper.class);


    UserDTO userToUserDTO(User user);
}
