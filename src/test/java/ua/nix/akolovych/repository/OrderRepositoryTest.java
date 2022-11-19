package ua.nix.akolovych.repository;

import ch.qos.logback.core.pattern.SpacePadder;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAllByClientId_shouldReturnAllClientOrders(){
        Client client = new Client(null, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        userRepository.save(client);
        Order order = new Order(null,client, Instant.now(), OrderStatus.UNFINISHED, new HashSet<>());
        orderRepository.save(order);

        client.getOrders().add(order);

        List<Order> expected = new ArrayList<>();
        expected.add(order);

        List<Order> actual = orderRepository.findAllByClientId(client.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void findAllByStatus_shouldReturnAllOrderBySomeStatus(){
        Client client = new Client(null, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        userRepository.save(client);
        Order order = new Order(null,client, Instant.now(), OrderStatus.UNFINISHED, new HashSet<>());
        orderRepository.save(order);

        client.getOrders().add(order);

        List<Order> expected = new ArrayList<>();
        expected.add(order);

        List<Order> actual = orderRepository.findAllByStatus(order.getStatus());

        assertEquals(expected, actual);
    }

    @Test
    public void deleteById_shouldDeleteOrder(){
        Client client = new Client(null, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        userRepository.save(client);
        Order order = new Order(null,client, Instant.now(), OrderStatus.UNFINISHED, new HashSet<>());
        orderRepository.save(order);

        client.getOrders().add(order);

        orderRepository.deleteById(order.getId());

        Optional <Order> actual = orderRepository.findById(order.getId());

        assertTrue(actual.isEmpty());
    }

    @Test
    public void deleteAllComponentsFromOrder_shouldDeleteComponentsFromOrder(){
        Component component = new Component(null,"NVIDIA RTX 3070", 21000,"Video card",
                "...", new HashSet<>(), new HashSet<>());
        componentRepository.save(component);

        Client client = new Client(null, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        userRepository.save(client);
        Order order = new Order(null,client, Instant.now(), OrderStatus.UNFINISHED, new HashSet<>());
        orderRepository.save(order);

        order.getComponents().add(component);
        client.getOrders().add(order);

        orderRepository.deleteAllComponentsFromOrder(order.getId());
        order.getClient().getOrders().remove(order);
        userRepository.save(order.getClient());
        orderRepository.deleteById(order.getId());

        Optional<Order> actual = orderRepository.findById(order.getId());

        assertTrue(actual.isEmpty());
    }
}
