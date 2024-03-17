package com.dersler.mycompany;

import com.dersler.mycompany.entity.RoleEntity;
import com.dersler.mycompany.enums.ERole;
import com.dersler.mycompany.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Startup implements CommandLineRunner {

    private RoleRepository roleRepository;

    public Startup(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Optional<RoleEntity> optRoleAdm = roleRepository.findByRoleName(ERole.ROLE_ADMIN);
        if(optRoleAdm.isEmpty()){
            RoleEntity re = new RoleEntity();
            re.setRoleName(ERole.ROLE_ADMIN);
            roleRepository.save(re);
        }

        Optional<RoleEntity> optRoleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR);
        if(optRoleMod.isEmpty()){
            RoleEntity re = new RoleEntity();
            re.setRoleName(ERole.ROLE_MODERATOR);
            roleRepository.save(re);
        }

        Optional<RoleEntity> optRoleUser = roleRepository.findByRoleName(ERole.ROLE_USER);
        if(optRoleUser.isEmpty()){
            RoleEntity re = new RoleEntity();
            re.setRoleName(ERole.ROLE_USER);
            roleRepository.save(re);
        }

    }
}
