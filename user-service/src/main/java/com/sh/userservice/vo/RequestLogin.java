package com.sh.userservice.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestLogin {

    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email cannot be less then two characters")
    @Email
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 4, message = "Password must be equal or greater than 4 characters")
    private String password;
}
