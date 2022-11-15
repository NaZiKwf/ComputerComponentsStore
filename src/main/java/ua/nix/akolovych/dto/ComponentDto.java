package ua.nix.akolovych.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.nix.akolovych.entity.Component;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComponentDto {

    private UUID id;

    private String name;

    private int price;

    private String description;

    private String specifications;


    public ComponentDto(Component component){
        this.id = component.getId();
        this.name = component.getName();
        this.price = component.getPrice();
        this.description = component.getDescription();
        this.specifications = component.getSpecifications();
    }

}
