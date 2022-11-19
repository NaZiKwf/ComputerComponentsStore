package ua.nix.akolovych.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import ua.nix.akolovych.dto.RegistrationDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.security.SecurityService;
import ua.nix.akolovych.service.impl.AuthServiceImpl;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private SecurityService securityService;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void validate_shouldReturnSuccessfulValidate(){
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setEmail("test@gmail.com");
        registrationDto.setLogin("test");

        given(userService.findByLogin(registrationDto.getLogin())).willReturn(null);
        given(securityService.existsByEmail(registrationDto.getEmail())).willReturn(false);


        boolean actual = authService.validate(new ModelMap(),registrationDto);

        assertTrue(actual);
    }

    @Test
    public void validate_shouldReturnNotSuccessfulValidate(){
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setEmail("test@gmail.com");
        registrationDto.setLogin("test");

        given(userService.findByLogin(registrationDto.getLogin())).willReturn(new Client());
        given(securityService.existsByEmail(registrationDto.getEmail())).willReturn(true);


        boolean actual = authService.validate(new ModelMap(),registrationDto);

        assertFalse(actual);
    }

}
