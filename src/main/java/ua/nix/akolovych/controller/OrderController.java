package ua.nix.akolovych.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ua.nix.akolovych.dto.OrderDto;
import ua.nix.akolovych.dto.UserDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.enums.OrderStatus;
import ua.nix.akolovych.service.OrderService;
import ua.nix.akolovych.service.UserService;

import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/order/")
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;


    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


    @PostMapping("create-new-order/{userId}/{componentId}")
    public String addNewComponentAndCreateNewOrder(ModelMap modelMap, @PathVariable UUID userId, @PathVariable UUID componentId){
        Client loggedInUser = (Client) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        modelMap.addAttribute("client-new-order",new UserDto(userService.findByLogin(loggedInUser.getUsername())));
        Order order = orderService.addNewOrder(userId);
        orderService.addComponentToOrder(componentId,order.getId());
        return "index";
    }

}
