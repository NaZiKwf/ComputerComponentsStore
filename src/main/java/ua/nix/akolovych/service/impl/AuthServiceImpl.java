package ua.nix.akolovych.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import ua.nix.akolovych.dto.AuthDto;
import ua.nix.akolovych.dto.RegistrationDto;
import ua.nix.akolovych.dto.UserDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.security.SecurityService;
import ua.nix.akolovych.service.AuthService;
import ua.nix.akolovych.service.UserService;

import java.util.Objects;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final SecurityService securityService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    public AuthServiceImpl(UserService userService, SecurityService securityService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.securityService = securityService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Client registration(RegistrationDto registrationDto) {

        UserDto user = new UserDto();
        user.setLogin(registrationDto.getLogin());
        user.setPassword(registrationDto.getPassword());
        user.setEmail(registrationDto.getEmail());

        Client regClient = userService.create(user);

        securityService.autoLogin(regClient.getLogin(),registrationDto.getPassword());

        return regClient;
    }

    @Override
    public void logout() {
        securityService.logout();
    }

    @Override
    public boolean validate(ModelMap modelMap, RegistrationDto registrationDto) {

        boolean successfulValidate = true;
        if(Objects.nonNull(userService.findByLogin(registrationDto.getLogin()))){
            modelMap.addAttribute("login", "Invalid login");
            successfulValidate=false;
        }

        if(securityService.existsByEmail(registrationDto.getEmail())){
            modelMap.addAttribute("email","Invalid email");
            successfulValidate=false;
        }

        return successfulValidate;
    }


}
