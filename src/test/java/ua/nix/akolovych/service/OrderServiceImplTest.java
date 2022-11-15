package ua.nix.akolovych.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.nix.akolovych.dto.OrderDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Component;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.enums.OrderStatus;
import ua.nix.akolovych.repository.ComponentRepository;
import ua.nix.akolovych.repository.OrderRepository;
import ua.nix.akolovych.repository.UserRepository;
import ua.nix.akolovych.service.impl.OrderServiceImpl;
import ua.nix.akolovych.utils.ConvertorForEntityAndDto;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private ComponentRepository componentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;


    @Test
    void getById_return_orderId() {
        UUID id = UUID.randomUUID();

        Order order = new Order(id,null, Instant.now(),null,new HashSet<>());

        given(orderRepository.findById(id)).willReturn(Optional.of(order));

        final Order expected = orderService.getById(id);

        Assertions.assertThat(expected).isNotNull();
    }

    @Test
    void getAll_should_returnOrders(){
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(UUID.randomUUID(),null,Instant.now(),null,new HashSet<>()));
        orders.add(new Order(UUID.randomUUID(),null,Instant.now(),null,new HashSet<>()));
        orders.add(new Order(UUID.randomUUID(),null,Instant.now(),null,new HashSet<>()));

        given(orderRepository.findAll()).willReturn(orders);

        List<Order> expected = orderService.getAll();

        assertEquals(expected, orders);
    }

    @Test
    void delete_should_removeOrder(){
        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "s323", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        Component component = new Component(UUID.randomUUID(),"NVIDIA RTX 3070",
                21000,"Video card","...", new HashSet<>(), new HashSet<>());
        Order order = new Order(UUID.randomUUID(),client, Instant.now(),null,Set.of(component));

        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));
        willDoNothing().given(orderRepository).deleteAllComponentsFromOrder(order.getId());
        given(userRepository.save(client)).willReturn(client);
        willDoNothing().given(orderRepository).deleteById(order.getId());

        orderService.delete(order.getId());

        verify(orderRepository,times(1)).deleteAllComponentsFromOrder(order.getId());
        verify(orderRepository,times(1)).deleteById(order.getId());
    }

    @Test
    void updateOrderStatus_should_returnOrderWithNewStatus(){
        Order order = new Order(UUID.randomUUID(),new Client(), Instant.now(),null,new HashSet<>());
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));
        orderService.updateOrderStatus(order.getId(),OrderStatus.IN_PROCESSING);

        Assertions.assertThat(order).isNotNull();

        verify(orderRepository).save(order);

    }

    @Test
    void addComponentToOrder_should_addComponentToUserOrder(){
        Component component = new Component(UUID.randomUUID(),"NVIDIA RTX 3070",
                21000,"Video card","...", new HashSet<>(), new HashSet<>());
        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "23232", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        Order order = new Order(UUID.randomUUID(),client, Instant.now(),null,new HashSet<>(Set.of(component)));
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));
        given(componentRepository.findById(component.getId())).willReturn(Optional.of(component));
        given(orderRepository.save(order)).willReturn(order);
        given(componentRepository.save(component)).willReturn(component);

        orderService.addComponentToOrder(component.getId(),order.getId());

        verify(orderRepository).save(order);
    }

    @Test
    void deleteComponentFromOrder_should_deleteComponentFromOrder(){
        Component component = new Component(UUID.randomUUID(),"NVIDIA RTX 3070",
                21000,"Video card","...", new HashSet<>(), new HashSet<>());
        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "23232", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        Order order = new Order(UUID.randomUUID(),client, Instant.now(),null,new HashSet<>(Set.of(component)));
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));
        given(componentRepository.findById(component.getId())).willReturn(Optional.of(component));
        given(orderRepository.save(order)).willReturn(order);
        given(componentRepository.save(component)).willReturn(component);

        orderService.deleteComponentFromOrder(component.getId(),order.getId());

        verify(orderRepository).save(order);
    }

    @Test
    void create_should_returnOrder(){
        Component component = new Component(UUID.randomUUID(),"NVIDIA RTX 3070",
                21000,"Video card","...", new HashSet<>(), new HashSet<>());
        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "23232", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        Order order = new Order(UUID.randomUUID(),client, Instant.now(),null,new HashSet<>(Set.of(component)));
        OrderDto orderDto = ConvertorForEntityAndDto.orderEntityToDto(order);
        given(userRepository.findById(client.getId())).willReturn(Optional.of(client));

        orderService.create(orderDto);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getAllOrdersByStatus_should_returnOrderByStatus(){
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(UUID.randomUUID(),null,Instant.now(),OrderStatus.IN_PROCESSING,new HashSet<>()));
        orders.add(new Order(UUID.randomUUID(),null,Instant.now(),OrderStatus.IN_PROCESSING,new HashSet<>()));
        orders.add(new Order(UUID.randomUUID(),null,Instant.now(),OrderStatus.IN_PROCESSING,new HashSet<>()));

        given(orderRepository.findAllByStatus(OrderStatus.IN_PROCESSING)).willReturn(orders);

        List<Order> expected = orderService.getAllOrdersByStatus(OrderStatus.IN_PROCESSING);

        assertEquals(expected,orders);

    }

    @Test
    void addNewOrderToClient_should_returnOrder(){
        Component component = new Component(UUID.randomUUID(),"NVIDIA RTX 3070",
                21000,"Video card","...", new HashSet<>(), new HashSet<>());
        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "23232", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        Order order = new Order(UUID.randomUUID(),client, Instant.now(),null,new HashSet<>(Set.of(component)));
        given(userRepository.findById(client.getId())).willReturn(Optional.of(client));
        given(orderRepository.save(order)).willReturn(order);
        given(userRepository.save(client)).willReturn(client);


    }
}