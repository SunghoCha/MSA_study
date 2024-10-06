package com.sh.userservice.service;

import com.sh.userservice.dto.UserDto;
import com.sh.userservice.jpa.UserEntity;
import com.sh.userservice.jpa.UserRepository;
import com.sh.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userEntity.setEncryptedPassword(encodedPassword);
        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(IllegalArgumentException::new);
        UserDto userDto = modelMapper.map(userEntity, UserDto.class);
        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);

        return userDto;
    }

    @Override
    public List<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 커스텀 예외 생략
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not fond with email: " + username));

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                true, true, true, true,
                new ArrayList<>());
    }
}
