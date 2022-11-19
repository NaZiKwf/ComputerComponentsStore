package ua.nix.akolovych.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.nix.akolovych.entity.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComponentDto {

    private UUID id;

    @NotBlank(message = "{NotBlank.ComponentDto.Name}")
    @Size(min=5, message = "{Size.ComponentDto.Name}")
    private String name;

    @NotNull
    @Min(value= 1, message = "{Min.ComponentDto.Price}")
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
