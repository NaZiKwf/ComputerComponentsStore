package ua.nix.akolovych.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.nix.akolovych.dto.ComponentDto;
import ua.nix.akolovych.dto.OrderDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.service.ComponentService;
import ua.nix.akolovych.service.UserService;

import java.util.ArrayList;
import java.util.Objects;

@Controller
@RequestMapping("/")
public class MainController {
    private final ComponentService componentService;

    private final UserService userService;

    public MainController(ComponentService componentService, UserService userService) {
        this.componentService = componentService;
        this.userService = userService;
    }


    @GetMapping
    public String getMainPage(ModelMap model){
        Authentication authenticationUser = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.nonNull(authenticationUser)){
            Client loggedInUser = userService.findByLogin(authenticationUser.getName());
            if(Objects.nonNull(loggedInUser)){
                model.addAttribute("userId", loggedInUser.getId());
            }
        }
        model.addAttribute("componentList", componentService.getAll()
                .stream()
                .map(ComponentDto::new).toList());
        return "index";
    }
}

