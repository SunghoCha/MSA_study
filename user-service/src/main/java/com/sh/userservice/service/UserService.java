package com.sh.userservice.service;

import com.sh.userservice.dto.UserDto;
import com.sh.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);
    UserDto getUserById(String userId);
    List<UserEntity> getUserByAll();
    UserDto getUserDetailsByEmail(String email);
}
