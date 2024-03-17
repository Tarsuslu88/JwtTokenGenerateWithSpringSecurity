package com.dersler.mycompany.controller;

import com.dersler.mycompany.dto.JwtReponseDTO;
import com.dersler.mycompany.dto.LoginDTO;
import com.dersler.mycompany.dto.SignupDTO;
import com.dersler.mycompany.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/openapi/v1")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/login")
    public ResponseEntity<JwtReponseDTO> login(@RequestBody LoginDTO loginDTO){
        JwtReponseDTO jwtReponseDTO = authenticationService.login(loginDTO);
        ResponseEntity re = new ResponseEntity(jwtReponseDTO, HttpStatus.OK);
        return re;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupDTO signupDTO){
        Long userId = authenticationService.signup(signupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User Registered Successfully with id : " + userId);
    }

}
