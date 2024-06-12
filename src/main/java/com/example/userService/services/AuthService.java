package com.example.userService.services;

import com.example.userService.dtos.UserDto;
import com.example.userService.model.User;
import com.example.userService.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public AuthService(UserRepository userRepository){
        this.userRepository=userRepository;

    }
    public ResponseEntity<UserDto> login(String email, String password){
        Optional<User> userOptional = userRepository.findByEmail(email);
//        response.getHeaders().add(HttpHeaders.SET_COOKIE, token);
      User user= userOptional.get();
      UserDto userDto= UserDto.from(user);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, HttpStatus.OK);
//        response.getHeaders().add(HttpHeaders.SET_COOKIE, token);

        return response;
    }
    public ResponseEntity<UserDto> singUp(String email,String password){
        User user=new User();
        user.setEmail(email);


//        user.setPassword(bCryptPasswordEncoder.encode(password));
        UserDto userDto=UserDto.from(user);
        userRepository.save(user);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
}
