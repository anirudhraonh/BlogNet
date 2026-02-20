package com.example.BlogNet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotBlank(message = "Username can't be blank.")
    @Size(min = 3, max = 20,message = "Username must have between  3-20 characters.")
    private String username;

    @NotBlank
    @Size(min = 5 , message = "Password must have more than 5 characters.")
    private String password;

    @NotBlank
    @Email(message = "Please enter valid email")
    private String email;

}
