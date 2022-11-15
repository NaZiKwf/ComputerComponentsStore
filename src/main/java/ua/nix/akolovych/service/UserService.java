package ua.nix.akolovych.service;

import ua.nix.akolovych.dto.UserDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Order;

import java.util.List;
import java.util.UUID;

public interface UserService {
    Client getById(UUID id);
    List<Client> getAll();
    Client create(UserDto userDto);
    Client update (UserDto userDto);
    void delete (UUID id);

    Client findByLogin(String login);

    List<Order> findAllUserOrders(UUID userId);

    void addComponentToFavourites(UUID componentId, UUID userId);

    void deleteComponentFromFavourites(UUID componentId, UUID userId);
}
