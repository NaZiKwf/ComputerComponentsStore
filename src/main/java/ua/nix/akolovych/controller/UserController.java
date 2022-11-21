package ua.nix.akolovych.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nix.akolovych.dto.ComponentDto;
import ua.nix.akolovych.dto.OrderDto;
import ua.nix.akolovych.dto.UserDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Component;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.enums.OrderStatus;
import ua.nix.akolovych.exception.EntityNotFoundException;
import ua.nix.akolovych.service.ComponentService;
import ua.nix.akolovych.service.OrderService;
import ua.nix.akolovych.service.UserService;
import ua.nix.akolovych.utils.ConvertorForEntityAndDto;


import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;


@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final ComponentService componentService;

    private final OrderService orderService;


    public UserController(UserService userService, ComponentService componentService, OrderService orderService) {
        this.userService = userService;
        this.componentService = componentService;
        this.orderService = orderService;
    }

    @PostMapping("/order-details/{orderId}")
    public String getComponentsFromOrder(ModelMap modelMap,@PathVariable UUID orderId){
        modelMap.addAttribute("componentsOrderList", componentService.getAllComponentsFromOrder(orderId));
        modelMap.addAttribute("orderId", orderId);
        return "index";
    }

    @GetMapping("/order-details/{orderId}")
    public String getAllComponentsFromUserOrder(ModelMap modelMap, @PathVariable UUID orderId){
        Client loggedInUser = (Client) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        if(Objects.nonNull(loggedInUser)){
            modelMap.addAttribute("componentsOrderList", componentService.getAllComponentsFromOrder(orderId)
                    .stream()
                    .map(ComponentDto::new)
                    .toList());
        }
        return "user/order-details";
    }


    @GetMapping("/my-orders")
    public String getAllUserOrders(ModelMap modelMap){
        Authentication authenticationUser = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.nonNull(authenticationUser)){
            Client loggedInUser = userService.findByLogin(authenticationUser.getName());
            if(Objects.nonNull(loggedInUser)) {
                modelMap.addAttribute("ordersList", userService.findAllUserOrders(loggedInUser.getId()).stream()
                        .map(OrderDto::new)
                        .toList());
            }
        }
        return "user/my-orders";
    }

    @GetMapping("/profile")
    public String profile(ModelMap modelMap){
        Client loggedInUser = (Client) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        modelMap.addAttribute("profile", new UserDto(userService.findByLogin(loggedInUser.getUsername())));
        return "user/profile";
    }

    @GetMapping("/change-profile")
    public String changeUserProfile(Model model) {
        model.addAttribute("changeProfile", new UserDto());
        return "user/change-profile";
    }

    @PostMapping("/change-profile")
    public String changeProfile(@ModelAttribute("changeProfile") @Valid UserDto userDto, BindingResult result){
        Client loggedInUser = (Client) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        Client client = userService.findByLogin(loggedInUser.getUsername());
        userDto.setId(client.getId());
        userDto.setLogin(client.getLogin());
        userDto.setPassword(client.getPassword());

        if(result.hasErrors()){
            return "user/change-profile";
        }

        if(Objects.isNull(userDto.getEmail())){
            userDto.setEmail(client.getEmail());
        }
        if(Objects.isNull(userDto.getFirstName())){
            userDto.setFirstName(client.getFirstName());
        }
        if(Objects.isNull(userDto.getLastName())){
            userDto.setLastName(client.getLastName());
        }
        if(Objects.isNull(userDto.getAddress())){
            userDto.setAddress(client.getAddress());
        }
        if(Objects.isNull(userDto.getPhone())){
            userDto.setPhone(client.getPhone());
        }
        userDto.setPassword(client.getPassword());
        userService.update(userDto);

        return "redirect:/user/profile";
    }

    @PostMapping("/my-orders/{orderId}")
    public String acceptOrder(ModelMap modelMap, @PathVariable UUID orderId){
        Order order = orderService.getById(orderId);
        if(Objects.equals(order.getStatus(),OrderStatus.UNFINISHED)){
            modelMap.addAttribute("confirmOrder",orderService.updateOrderStatus(orderId,OrderStatus.IN_PROCESSING));
        }
       return "redirect:";
    }

    @PostMapping("/component/{componentId}")
    public String component(ModelMap modelMap, @PathVariable UUID componentId){
        Component component = componentService.getById(componentId);
        ComponentDto componentDto = ConvertorForEntityAndDto.componentEntityToDto(component);
        modelMap.addAttribute("componentPage", componentDto);
        return "user/component";
    }

    @GetMapping("/change-component/{componentId}")
    public String changesComponent(Model model, @PathVariable("componentId") UUID componentId){
        model.addAttribute("changeComponent",new ComponentDto());
        model.addAttribute("currentComponentId", componentId);
        return "user/change-component";
    }

    @PostMapping("/change-component/{componentId}")
    public String updateComponent (ModelMap modelMap, @ModelAttribute("changeComponent") @Valid ComponentDto componentDto,
                                   BindingResult result, @PathVariable("componentId") UUID componentId){
        Component component = componentService.getById(componentId);
        componentDto.setId(component.getId());

        if(result.hasErrors()){
            return "user/change-component";
        }
        if(Objects.isNull(componentDto.getName())){
            componentDto.setName(component.getName());
        }
        if(Objects.isNull(componentDto.getPrice())){
            componentDto.setPrice(component.getPrice());
        }
        if(Objects.isNull(componentDto.getDescription())){
            componentDto.setDescription(component.getDescription());
        }
        if(Objects.isNull(componentDto.getSpecifications())){
            componentDto.setSpecifications(component.getSpecifications());
        }
            componentService.update(componentDto);

        return "redirect:/";
    }

    @GetMapping("/all-orders")
    public String getAllUsersOrders(ModelMap modelMap){
        modelMap.addAttribute("allOrders", orderService.getAllOrdersByStatus(OrderStatus.IN_PROCESSING).stream()
                .map(OrderDto::new)
                .toList());
        return "user/all-orders";
    }

    @PostMapping("/all-orders/{orderId}")
    public String acceptUserOrderByAdmin(ModelMap modelMap, @PathVariable UUID orderId){
        Order order = orderService.getById(orderId);
        if(Objects.equals(order.getStatus(),OrderStatus.IN_PROCESSING)){
            try {
                modelMap.addAttribute("confirmUserOrder", orderService.updateOrderStatus(orderId, OrderStatus.ACCEPTED));
            }
            catch (EntityNotFoundException e){
                modelMap.addAttribute("error",e.getMessage());
                return "user/all-orders";
            }
        }
        return "redirect:";
    }

    @PostMapping("/all-orders-cancel/{orderId}")
    public String cancelUserOrderByAdmin(ModelMap modelMap, @PathVariable UUID orderId){
        Order order = orderService.getById(orderId);
        if (Objects.equals(order.getStatus(),OrderStatus.IN_PROCESSING)){
            try {
                modelMap.addAttribute("cancelUserOrder", orderService.updateOrderStatus(orderId,OrderStatus.CANCELED));
            }
            catch (EntityNotFoundException e){
                modelMap.addAttribute("error", e.getMessage());
                return "user/all-orders";
            }
        }
        return "redirect:/user/all-orders";
    }

    @GetMapping("/order-details-delete/{componentId}/{orderId}")
    public String deleteComponentFromOrder(@PathVariable UUID componentId, @PathVariable UUID orderId){
        orderService.deleteComponentFromOrder(componentId, orderId);
        return "redirect:/user/my-orders";
    }

    @GetMapping("/my-orders-delete/{orderId}")
    public String deleteOrderByUser(ModelMap modelMap, @PathVariable UUID orderId){
        orderService.delete(orderId);
        return "redirect:/user/my-orders";
    }

    @GetMapping("/user-order-details/{orderId}")
    public String getComponentsFromUserOrder(ModelMap modelMap, @PathVariable UUID orderId){
        Client loggedInUser = (Client) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        if(Objects.nonNull(loggedInUser)){
            modelMap.addAttribute("userComponentsOrderList", componentService.getAllComponentsFromOrder(orderId)
                    .stream()
                    .map(ComponentDto::new)
                    .toList());

        }
        return "user/user-order-details";
    }

    @GetMapping("/new-component")
    public String addNewComponent(Model model) {
        model.addAttribute("addNewComponent", new ComponentDto());
        return "user/new-component";
    }

    @PostMapping("/new-component")
    public String addNewComponent(@ModelAttribute("addNewComponent") @Valid ComponentDto componentDto, BindingResult result, ModelMap modelMap) {
        if(result.hasErrors()){
            return "user/new-component";
        }
        componentService.create(componentDto);
        return "redirect:/";
    }

}

