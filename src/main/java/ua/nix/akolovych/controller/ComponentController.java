package ua.nix.akolovych.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ua.nix.akolovych.dto.ComponentDto;
import ua.nix.akolovych.exception.EntityNotFoundException;
import ua.nix.akolovych.service.ComponentService;

import java.util.UUID;

@Controller
@RequestMapping("/components/")
public class ComponentController {

    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }


    @PostMapping("add-component")
    public String addComponent(ModelMap modelMap, @RequestBody ComponentDto componentDto){
        modelMap.addAttribute("add-component", new ComponentDto(componentService.create(componentDto)));
        return "index";
    }

    @DeleteMapping("delete-component/{componentId}")
    public String deleteComponent(@PathVariable UUID componentId){
        componentService.delete(componentId);
        return "index";
    }

    @GetMapping("componentList")
    public String getAllComponents(ModelMap modelMap){
        modelMap.addAttribute("componentList", componentService.getAll()
                .stream()
                .map(ComponentDto::new)
                .toList());
        return "index";
    }

}
