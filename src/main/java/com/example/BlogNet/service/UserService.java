package com.example.BlogNet.service;

import com.example.BlogNet.dto.LoginRequestDTO;
import com.example.BlogNet.dto.UserRequestDTO;
import com.example.BlogNet.dto.UserResponseDTO;
import com.example.BlogNet.entity.User;
import com.example.BlogNet.exception.DuplicateUserException;
import com.example.BlogNet.exception.ResourceNotFoundExcecption;
import com.example.BlogNet.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
   private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO register(UserRequestDTO userRequestDTO){
        //User duplicate?
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new DuplicateUserException("Username already taken.");
        }

        //email duplicate
        if (userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw new DuplicateUserException("Email already taken.");
        }

        //Create user
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setEmail(userRequestDTO.getEmail());

        //save to db
       User savedUser = userRepository.save(user);

        return convertToResponseDTO(savedUser);
    }

    public UserResponseDTO login(LoginRequestDTO loginRequestDTO, HttpServletRequest request){
        //Find user
        User user = userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow(() -> new ResourceNotFoundExcecption("Invalid username"));

        //check pwd
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())){
            throw new ResourceNotFoundExcecption("Invalid Password");
        }

        //create session
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Save session
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        return convertToResponseDTO(user);
    }

    //HELPER
    public UserResponseDTO convertToResponseDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setCreatedAt(user.getCreatedAt());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setRole(user.getRole());

        return userResponseDTO;
    }
}
