package com.sh.userservice.controller;

import com.sh.userservice.config.MapperConfig;
import com.sh.userservice.dto.UserDto;
import com.sh.userservice.jpa.UserEntity;
import com.sh.userservice.service.UserService;
import com.sh.userservice.service.UserServiceImpl;
import com.sh.userservice.vo.Greeting;
import com.sh.userservice.vo.RequestUser;
import com.sh.userservice.vo.ResponseUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service")
public class UserController {

    private final Environment env;
    private final Greeting greeting;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MapperConfig mapperConfig;
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in User Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome() {
        //return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@Valid @RequestBody RequestUser requestUser) {
        UserDto userDto = modelMapper.map(requestUser, UserDto.class);
        UserDto newUserDto = userService.createUser(userDto);
        ResponseUser responseUser = modelMapper.map(newUserDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        List<ResponseUser> allUsers = userService.getUserByAll().stream()
                .map(user -> modelMapper.map(user, ResponseUser.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(allUsers);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable(name ="userId") String userId) {
        UserDto userDto = userService.getUserById(userId);
        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);

        return ResponseEntity.ok().body(responseUser);
    }
}
