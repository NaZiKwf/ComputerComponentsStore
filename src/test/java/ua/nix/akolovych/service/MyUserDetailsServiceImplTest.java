package ua.nix.akolovych.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import ua.nix.akolovych.dto.UserDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.repository.UserRepository;
import ua.nix.akolovych.service.impl.MyUserDetailsServiceImpl;
import ua.nix.akolovych.service.impl.UserServiceImpl;
import ua.nix.akolovych.utils.ConvertorForEntityAndDto;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceImplTest {

    @Mock
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private MyUserDetailsServiceImpl myUserDetailsService;

    @Test
    public void loadUserByUsername_shouldReturnUser(){
        String login = "test123";

        given(userRepository.findByLogin(login)).willReturn(Optional.of(new Client()));

        UserDetails actual = myUserDetailsService.loadUserByUsername(login);

        Assertions.assertNotNull(actual);

    }

    @Test
    public void addNewUser_shouldCreateNewUser(){
        Client client = new Client(null, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "23232", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        UserDto user = ConvertorForEntityAndDto.userEntityToDto(client);
        given(userService.create(user)).willReturn(client);
        myUserDetailsService.addNewUser(user);

        verify(userService).create(user);
    }

}
