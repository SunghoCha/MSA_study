package com.sh.userservice.dto;

import com.sh.userservice.vo.ResponseOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class UserDto {

    private String email;
    private String name;
    private String password;
    private String userId;
    private Date createdAt;
    private String encryptedPassword;

    private List<ResponseOrder> orders;
}
