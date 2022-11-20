package ua.nix.akolovych.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.nix.akolovych.dto.OrderDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Component;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.enums.OrderStatus;
import ua.nix.akolovych.exception.EntityNotFoundException;
import ua.nix.akolovych.repository.ComponentRepository;
import ua.nix.akolovych.repository.OrderRepository;
import ua.nix.akolovych.repository.UserRepository;
import ua.nix.akolovych.service.OrderService;

import java.time.Instant;
import java.util.*;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ComponentRepository componentRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ComponentRepository componentRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.componentRepository = componentRepository;
    }

    @Override
    public Order getById(UUID id) {
        return  orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order create(OrderDto orderDto) {
        Order order = new Order();
        Client client = userRepository.findById(orderDto.getUserId()).orElse(null);
        if(Objects.isNull(client)){
            log.warn("Client not found");
            throw new EntityNotFoundException("not found...");
        }
        order.setStatus(OrderStatus.UNFINISHED);
        order.setDate(Instant.now());
        order.setClient(client);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(Objects.nonNull(order)){
            if(order.getComponents().size() > 0){
                orderRepository.deleteAllComponentsFromOrder(id);
                order.getClient().getOrders().remove(order);
                userRepository.save(order.getClient());
            }
            orderRepository.deleteById(id);
        }
    }

    @Override
    public Order updateOrderStatus(UUID id, OrderStatus orderStatus) {
        Order order = getById(id);
        if(Objects.isNull(order)){
            log.warn("Order not found");
            throw new EntityNotFoundException("This order doesn't exist");
        }
        order.setStatus(orderStatus);

        return orderRepository.save(order);

    }

    @Override
    public void addComponentToOrder(UUID componentId, UUID orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(Objects.nonNull(order)){
            Component component = componentRepository.findById(componentId).orElse(null);
            if(Objects.nonNull(component)){
                order.getComponents().add(component);
                component.getOrders().add(order);
                orderRepository.save(order);
                componentRepository.save(component);
            }
        }

    }

    @Override
    public void deleteComponentFromOrder(UUID componentId, UUID orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(Objects.nonNull(order)){
            Component component = componentRepository.findById(componentId).orElse(null);
            if(Objects.nonNull(component)){
                order.getComponents().remove(component);
                component.getOrders().remove(order);
                orderRepository.save(order);
                componentRepository.save(component);
            }
        }
    }

    @Override
    public Order addNewOrder(UUID userId) {
        Client client = userRepository.findById(userId).orElse(null);
        if(Objects.isNull(client)){
            return null;
        }
        Set<Order> orders = client.getOrders();
        if(orders.isEmpty()){
            return addNewOrderToClient(client);
        }
        else {
            Iterator<Order> order1 = client.getOrders()
                    .stream()
                    .filter(order -> order.getStatus() == OrderStatus.UNFINISHED)
                    .iterator();
            if(order1.hasNext()){
                return order1.next();
            }
            else {
                return addNewOrderToClient(client);
            }
        }

    }

    @Override
    public List<Order> getAllOrdersByStatus(OrderStatus orderStatus) {
        return orderRepository.findAllByStatus(orderStatus);
    }


    private Order addNewOrderToClient(Client client){
        OrderDto orderDto = OrderDto.builder()
                .date(Instant.now())
                .status(OrderStatus.UNFINISHED)
                .userId(client.getId())
                .build();
        Order order = create(orderDto);
        client.getOrders().add(order);
        userRepository.save(client);
        return order;
    }

}
