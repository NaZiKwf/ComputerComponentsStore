package ua.nix.akolovych.service;

import org.springframework.ui.ModelMap;
import ua.nix.akolovych.dto.AuthDto;
import ua.nix.akolovych.dto.RegistrationDto;
import ua.nix.akolovych.entity.Client;

public interface AuthService {

    Client registration(RegistrationDto registrationDto);
    void logout();
    boolean validate(ModelMap modelMap, RegistrationDto registrationDto);
}

