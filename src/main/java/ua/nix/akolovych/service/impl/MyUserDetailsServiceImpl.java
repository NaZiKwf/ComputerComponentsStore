package ua.nix.akolovych.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.nix.akolovych.dto.UserDto;
import ua.nix.akolovych.repository.UserRepository;
import ua.nix.akolovych.service.MyUserDetailsService;
import ua.nix.akolovych.service.UserService;

@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsService {
    private final UserService userService;
    private final UserRepository userRepository;

    public MyUserDetailsServiceImpl(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Login was not found!"));
    }


    @Override
    public void addNewUser(UserDto userDto) {
        userService.create(userDto);
    }
}
