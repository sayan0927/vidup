package com.example.videostreamingcore.core.entity_dtos;

import com.example.videostreamingcore.core.entity.DatabaseEntities.User;
import com.example.videostreamingcore.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {


  UserMapper INSTANCE =   Mappers.getMapper(UserMapper.class);


    UserDTO userToUserDTO(User user);
}
