package ua.nix.akolovych.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.nix.akolovych.dto.UserDto;

public interface MyUserDetailsService extends UserDetailsService {

    void addNewUser(UserDto userDto);
}
