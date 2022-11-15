package ua.nix.akolovych.utils;

import ua.nix.akolovych.dto.ComponentDto;
import ua.nix.akolovych.dto.OrderDto;
import ua.nix.akolovych.dto.UserDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Component;
import ua.nix.akolovych.entity.Order;


public final class ConvertorForEntityAndDto {
    public static ComponentDto componentEntityToDto(Component entity) {
        return new ComponentDto(entity.getId(),entity.getName(),entity.getPrice(),
                entity.getDescription(),entity.getSpecifications());
    }

    public static OrderDto orderEntityToDto(Order entity){
        return new OrderDto(entity.getId(), entity.getClient().getLogin(), entity.getClient().getId(), entity.getDate(),entity.getStatus());
    }

    public static UserDto userEntityToDto(Client entity){
        return new UserDto(entity.getId(),entity.getFirstName(),entity.getLastName(),
                entity.getPhone(), entity.getEmail(),
                entity.getAddress(), entity.getLogin(), entity.getPassword());
    }

    public static Component componentDtoToEntity(ComponentDto dto) {
        return Component.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .specifications(dto.getSpecifications())
                .build();
    }

    public static Order orderDtoToEntity(OrderDto dto){
        return Order.builder()
                .id(dto.getId())
                .id(dto.getUserId())
                .date(dto.getDate())
                .status(dto.getStatus())
                .build();
    }

    public static Client userDtoToEntity(UserDto dto){
        return Client.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .login(dto.getLogin())
                .password(dto.getPassword())
                .build();
    }

}
