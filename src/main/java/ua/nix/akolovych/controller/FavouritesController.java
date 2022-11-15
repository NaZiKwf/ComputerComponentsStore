package ua.nix.akolovych.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ua.nix.akolovych.dto.ComponentDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.service.ComponentService;
import ua.nix.akolovych.service.UserService;

import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/favourites/")
public class FavouritesController {

    private final UserService userService;

    private final ComponentService componentService;

    public FavouritesController(UserService userService, ComponentService componentService) {
        this.userService = userService;
        this.componentService = componentService;
    }


    @GetMapping("my-favourites")
    public String getAllUserFavourites(ModelMap modelMap){
        Client loggedInUser = (Client) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        if(Objects.nonNull(loggedInUser)){
            modelMap.addAttribute("userId",loggedInUser.getId());
            modelMap.addAttribute("userFavouritesList", componentService.getAllFavouritesComponents(loggedInUser.getId())
                    .stream()
                    .map(ComponentDto::new)
                    .toList());
        }
        return "favourites/my-favourites";
    }

    @PostMapping("favourites-add/{componentId}/{userId}")
    public String addComponentToFavourites(@PathVariable UUID componentId,@PathVariable UUID userId){
        userService.addComponentToFavourites(componentId,userId);
        return "index";
    }

    @GetMapping("favourites-delete/{componentId}/{userId}")
    public String deleteComponentFromFavourites(ModelMap modelMap,@PathVariable UUID componentId, @PathVariable UUID userId){
        userService.deleteComponentFromFavourites(componentId,userId);
        return "index";
    }
}
