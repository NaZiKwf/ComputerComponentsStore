package ua.nix.akolovych.service;


import ua.nix.akolovych.dto.ComponentDto;
import ua.nix.akolovych.dto.FilterDto;
import ua.nix.akolovych.entity.Component;

import java.util.List;
import java.util.UUID;

public interface ComponentService {
    Component getById(UUID id);
    List<Component> getAll();
    Component create(ComponentDto componentDto);
    Component update (ComponentDto componentDto);
    void delete (UUID id);
    List<Component> getAllComponentsFromOrder(UUID orderId);
    List<Component> getAllFilteredComponents(FilterDto filterDto);

    List<Component> getAllFavouritesComponents(UUID userId);
}
