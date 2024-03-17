package com.dersler.mycompany.service;

import com.dersler.mycompany.dto.ErrorDTO;
import com.dersler.mycompany.dto.JwtReponseDTO;
import com.dersler.mycompany.dto.LoginDTO;
import com.dersler.mycompany.dto.SignupDTO;
import com.dersler.mycompany.entity.RoleEntity;
import com.dersler.mycompany.entity.UserEntity;
import com.dersler.mycompany.enums.ERole;
import com.dersler.mycompany.exception.BusinessException;
import com.dersler.mycompany.repository.RoleRepository;
import com.dersler.mycompany.repository.UserRepository;
import com.dersler.mycompany.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                     PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public JwtReponseDTO login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtil.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(role->role.getAuthority()).collect(Collectors.toList());

        JwtReponseDTO jwtResponseDTO = new JwtReponseDTO();
        jwtResponseDTO.setFirstName(userDetails.getFirstName());
        jwtResponseDTO.setLastName(userDetails.getLastName());
        jwtResponseDTO.setToken(jwtToken);
        jwtResponseDTO.setRoles(roles);
        jwtResponseDTO.setId(userDetails.getId());

        return jwtResponseDTO;
    }

    @Override
    public Long signup(SignupDTO signupDTO) {
        List<ErrorDTO> errorDTOS = new ArrayList<>();
        if(userRepository.existsByEmail(signupDTO.getEmail())){
            errorDTOS.add(new ErrorDTO("AUTH_001", "Email already exist"));
            throw new BusinessException(errorDTOS);
        }
        String encodedPassword = passwordEncoder.encode(signupDTO.getPassword());
        Set<RoleEntity> roleEntities = new HashSet<>();
        Optional<RoleEntity> optRole;

        if(signupDTO.getRole() != null && signupDTO.getRole().equals("ADMIN")){
            optRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN);
        }else if(signupDTO.getRole() != null && signupDTO.getRole().equals("MODERATOR")){
            optRole = roleRepository.findByRoleName(ERole.ROLE_MODERATOR);
        }else{
            optRole = roleRepository.findByRoleName(ERole.ROLE_USER);
        }

        roleEntities.add(optRole.get());

        UserEntity user = new UserEntity();
        user.setEmail(signupDTO.getEmail());
        user.setFirstName(signupDTO.getFirstName());
        user.setLastName(signupDTO.getLastName());
        user.setPhone(signupDTO.getPhone());
        user.setPassword(encodedPassword);
        user.setRoles(roleEntities);
        user.setCreateDate(new Date());

        user = userRepository.save(user);
        return user.getId();
    }
}
