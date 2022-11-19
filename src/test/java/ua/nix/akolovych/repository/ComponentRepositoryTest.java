package ua.nix.akolovych.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Component;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.enums.OrderStatus;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ComponentRepositoryTest {
    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void existByEmail_shouldReturnTrueOrFalse(){
        Component component = new Component(null,"NVIDIA RTX 3070", 21000,"Video card","...", new HashSet<>(), new HashSet<>());

        componentRepository.save(component);

        boolean actual = componentRepository.existsByName(component.getName());

        assertTrue(actual);
    }

    @Test
    public void findAllByOrderId_shouldReturnListComponentsFromOrder(){
        Client client = new Client(null, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());

        userRepository.save(client);

        Component component = new Component(null,"NVIDIA RTX 3070", 21000,"Video card",
                "...", new HashSet<>(), new HashSet<>());
        componentRepository.save(component);

        Order order = new Order(null,client, Instant.now(), OrderStatus.UNFINISHED, Set.of(component));

        orderRepository.save(order);

        List<Component> expected = new ArrayList<>();
        expected.add(component);

        List<Component> actual = componentRepository.findAllByOrderId(order.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void findAllUserFavouritesComponents_shouldReturnListOfFavouritesComponents(){
        Client client = new Client(null, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());

        userRepository.save(client);

        Component component = new Component(null,"NVIDIA RTX 3070", 21000,"Video card",
                "...", new HashSet<>(), new HashSet<>());
        componentRepository.save(component);

        client.getComponents().add(component);
        component.getClients().add(client);
        userRepository.save(client);
        componentRepository.save(component);

        List<Component> expected = new ArrayList<>();
        expected.add(component);

        List<Component> actual = componentRepository.findAllUserFavouritesComponents(client.getId());

        assertEquals(expected, actual);
    }
}
