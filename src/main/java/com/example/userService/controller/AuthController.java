package com.example.userService.controller;

import com.example.userService.dtos.LoginRequestDto;
import com.example.userService.dtos.SignUpRequestDto;
import com.example.userService.dtos.UserDto;
import com.example.userService.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }
@PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto){
    return authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());

    }

    @PostMapping("/singup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        return authService.singUp(signUpRequestDto.getEmail(),signUpRequestDto.getPassword());

    }


}
