package com.example.userService.services;

import com.example.userService.dtos.UserDto;
import com.example.userService.model.Session;
import com.example.userService.model.SessionStatus;
import com.example.userService.model.User;
import com.example.userService.repository.SessionRepository;
import com.example.userService.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
   private BCryptPasswordEncoder bCryptPasswordEncoder;
   private SessionRepository sessionRepository;
    public AuthService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,SessionRepository sessionRepository){
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.sessionRepository=sessionRepository;

    }
    public ResponseEntity<UserDto> login(String email, String password){
        Optional<User> userOptional = userRepository.findByEmail(email);
//        response.getHeaders().add(HttpHeaders.SET_COOKIE, token);


        User user= userOptional.get();
        if(user==null)
        {
            throw new RuntimeException("user is wrong");
        }
      if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
          throw new RuntimeException("password is wrong");
      }
        String token= RandomStringUtils.randomAlphabetic(30);
        MacAlgorithm alg= Jwts.SIG.HS256;
        SecretKey key = alg.key().build();
        Map<String,Object> jsonForJwt=new HashMap<>();
        jsonForJwt.put("email",user.getEmail());
        jsonForJwt.put("createdAt",new Date());
        jsonForJwt.put("expiryAt",new Date(LocalDate.now().plusDays(3).toEpochDay()));
        token=Jwts.builder().claims(jsonForJwt).signWith(key,alg).compact();

        Session session=new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);


      UserDto userDto= UserDto.from(user);
        MultiValueMapAdapter<String,String>headers= new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE,"auth-token:"+token);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto,headers,HttpStatus.OK);
//        response.getHeaders().add(HttpHeaders.SET_COOKIE, token);

        return response;
    }
    public ResponseEntity<UserDto> singUp(String email,String password){
        User user=new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        UserDto userDto=UserDto.from(user);
        userRepository.save(user);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
}
