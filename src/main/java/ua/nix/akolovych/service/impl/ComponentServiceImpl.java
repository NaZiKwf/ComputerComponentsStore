package ua.nix.akolovych.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nix.akolovych.dto.ComponentDto;
import ua.nix.akolovych.dto.FilterDto;
import ua.nix.akolovych.entity.Component;
import ua.nix.akolovych.exception.EntityNotFoundException;
import ua.nix.akolovych.repository.ComponentRepository;
import ua.nix.akolovych.service.ComponentService;
import ua.nix.akolovych.utils.ConvertorForEntityAndDto;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class ComponentServiceImpl implements ComponentService {

    private final ComponentRepository componentRepository;

    @Autowired
    public ComponentServiceImpl( ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Override
    public Component getById(UUID id) {
        return componentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Component> getAll() {
        return componentRepository.findAll();
    }

    @Override
    public Component create(ComponentDto componentDto) {
       if(componentRepository.existsByName(componentDto.getName())){
           log.warn("Component not found");
           throw new EntityNotFoundException("This component is already exist");
       }
       componentDto.setId(null);
       Component component = ConvertorForEntityAndDto.componentDtoToEntity(componentDto);
       return componentRepository.save(component);
    }

    @Override
    public Component update(ComponentDto componentDto) {
        if(Objects.isNull(getById(componentDto.getId()))){
            log.warn("Component not found");
            throw new EntityNotFoundException("This component doesn't exist");
        }
        return componentRepository.save(ConvertorForEntityAndDto.componentDtoToEntity(componentDto));
    }

    @Override
    public void delete(UUID id) {
        componentRepository.deleteById(id);
    }

    @Override
    public List<Component> getAllComponentsFromOrder(UUID orderId) {
        return componentRepository.findAllByOrderId(orderId);
    }

    @Override
    public List<Component> getAllFilteredComponents(FilterDto filterDto) {
        String query = filterDto.getQuery().toLowerCase().trim();
        List<Component> allComponent = componentRepository.findAll();

        if (filterDto.getSearchBy().equals("title"))
            return allComponent.stream()
                    .filter(f -> f.getName().toLowerCase().trim().contains(query))
                    .toList();

        return allComponent;
    }

    @Override
    public List<Component> getAllFavouritesComponents(UUID userId) {
        return componentRepository.findAllUserFavouritesComponents(userId);
    }


}
