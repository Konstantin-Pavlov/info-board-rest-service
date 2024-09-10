package com.aston.infoBoardRestService.mapper;

import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDTO(User user);

    User ToUser(UserDto userDTO);

    List<UserDto> ToUserDtoList(List<User> userList);

    List<User> toUserList(List<UserDto> userDTOList);
}