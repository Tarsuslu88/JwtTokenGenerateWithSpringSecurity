package com.dersler.mycompany.service;

import com.dersler.mycompany.dto.JwtReponseDTO;
import com.dersler.mycompany.dto.LoginDTO;
import com.dersler.mycompany.dto.SignupDTO;


public interface AuthenticationService {

    public JwtReponseDTO login(LoginDTO loginDTO);

    public Long signup(SignupDTO signupDTO);


}
