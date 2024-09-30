package com.sh.userservice.service;

import com.sh.userservice.dto.UserDto;
import com.sh.userservice.jpa.UserEntity;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);
    UserDto getUserById(String userId);
    List<UserEntity> getUserByAll();
}
