package ua.nix.akolovych.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.nix.akolovych.dto.ComponentDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Component;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.enums.OrderStatus;
import ua.nix.akolovych.repository.ComponentRepository;
import ua.nix.akolovych.service.impl.ComponentServiceImpl;
import ua.nix.akolovych.utils.ConvertorForEntityAndDto;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ComponentServiceImplTest {

    private ConvertorForEntityAndDto convertor;
    @Mock
    private ComponentRepository componentRepository;

    @InjectMocks
    private ComponentServiceImpl componentService;

    @Test
    void getById_return_orderId() {
        UUID id = UUID.randomUUID();

        Component component = new Component(id,"NVIDIA RTX 3070", 21000,"Video card","...", new HashSet<>(), new HashSet<>());

        given(componentRepository.findById(id)).willReturn(Optional.of(component));

        final Component expected = componentService.getById(id);

        Assertions.assertThat(expected).isNotNull();
    }

    @Test
    void getAll_should_returnOrders(){
        List<Component> components = new ArrayList<>();
        components.add(new Component(UUID.randomUUID(),"NVIDIA RTX 3070", 21000,"Video card","...", new HashSet<>(), new HashSet<>()));
        components.add(new Component(UUID.randomUUID(),"NVIDIA RTX 3060", 18000,"Video card","...", new HashSet<>(), new HashSet<>()));
        components.add(new Component(UUID.randomUUID(),"NVIDIA RTX 3050ti", 14000,"Video card","...", new HashSet<>(), new HashSet<>()));

        given(componentRepository.findAll()).willReturn(components);

        List<Component> expected = componentService.getAll();

        assertEquals(expected, components);
    }

    @Test
    void delete_should_removeComponent(){
        UUID id = UUID.randomUUID();

        willDoNothing().given(componentRepository).deleteById(id);
        componentService.delete(id);

        verify(componentRepository,times(1)).deleteById(id);
    }

    @Test
    void create_should_returnComponent(){
        UUID id = UUID.randomUUID();
        Component component = new Component(id,"NVIDIA RTX 3070",
                21000,"Video card","...", new HashSet<>(), new HashSet<>());
        ComponentDto componentDto = ConvertorForEntityAndDto.componentEntityToDto(component);
        given(componentRepository.save(any(Component.class))).willReturn(component);
        given(componentRepository.existsByName(componentDto.getName())).willReturn(false);

        Component savedComponent = componentService.create(componentDto);

        Assertions.assertThat(savedComponent).isNotNull();

        verify(componentRepository).save(any(Component.class));
    }

       @Test
    void update_should_returnComponent(){
        Component component = new Component(UUID.randomUUID(),"NVIDIA RTX 3070", 21000,
                "Video card","...", new HashSet<>(), new HashSet<>());
        given(componentRepository.save(any(Component.class))).willReturn(component);
        given(componentRepository.findById(component.getId())).willReturn(Optional.of(component));
        Component actual = componentService.update(ConvertorForEntityAndDto.componentEntityToDto(component));

        Assertions.assertThat(actual).isNotNull();

        verify(componentRepository).save(any(Component.class));

    }

    @Test
    void getAllComponentsFromOrder_should_returnAllComponentsInOrder(){
        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        Component component = new Component(UUID.randomUUID(),"NVIDIA RTX 3070", 21000,
                "Video card","...", new HashSet<>(), new HashSet<>());
        Order order = new Order(UUID.randomUUID(),client, Instant.now(), OrderStatus.UNFINISHED, new HashSet<>());
        client.getOrders().add(new Order(UUID.randomUUID(),null, Instant.now(),null, Set.of(component)));
        given(componentRepository.findAllByOrderId(order.getId())).willReturn(List.of(component));
        List<Component> actual = componentService.getAllComponentsFromOrder(order.getId());

        Assertions.assertThat(actual).hasSize(1);
    }

    @Test
    void getAllFavouritesComponents_should_returnAllUserFavouriteComponents(){
        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        Component component = new Component(UUID.randomUUID(),"NVIDIA RTX 3070", 21000,
                "Video card","...", new HashSet<>(), new HashSet<>());
        given(componentRepository.findAllUserFavouritesComponents(client.getId())).willReturn(List.of(component));

        List <Component> actual = componentService.getAllFavouritesComponents(client.getId());

        Assertions.assertThat(actual).hasSize(1);
    }
}
