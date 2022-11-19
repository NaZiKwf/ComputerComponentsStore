package ua.nix.akolovych.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nix.akolovych.dto.AuthDto;
import ua.nix.akolovych.dto.RegistrationDto;
import ua.nix.akolovych.dto.UserDto;
import ua.nix.akolovych.service.AuthService;
import ua.nix.akolovych.service.MyUserDetailsService;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("auth/")
public class AuthController {

    private final MyUserDetailsService userDetailsService;
    private final AuthService authService;

    public AuthController(MyUserDetailsService userDetailsService, AuthService authService) {
        this.userDetailsService = userDetailsService;
        this.authService = authService;
    }

    @GetMapping("registration")
    public String registration(Model model) {
        model.addAttribute("registrationForm", new RegistrationDto());
        return "registration";
    }

    @PostMapping("registration")
    public String registration(@ModelAttribute("registrationForm") @Valid RegistrationDto registrationDto, BindingResult result, ModelMap modelMap) {
       if(result.hasErrors()){
           return "registration";
       }
        if (authService.validate(modelMap, registrationDto)) {
                UserDto userDto = UserDto.builder()
                        .login(registrationDto.getLogin())
                        .password(registrationDto.getPassword())
                        .email(registrationDto.getEmail())
                        .build();
                userDetailsService.addNewUser(userDto);
                return "redirect:/login";
        }
        return "registration";
    }

    @GetMapping("logout")
    public String logout(){
        authService.logout();
        return "index";
    }
}
