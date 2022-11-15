package ua.nix.akolovych.service;

import ua.nix.akolovych.dto.OrderDto;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.enums.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    Order getById(UUID id);
    List<Order> getAll();
    Order create(OrderDto orderDto);
    void delete (UUID id);
   Order updateOrderStatus(UUID id, OrderStatus orderStatus);

    void addComponentToOrder(UUID componentId, UUID orderID);

    void deleteComponentFromOrder(UUID componentId, UUID orderId);

    Order addNewOrder(UUID userId);

    List <Order> getAllOrdersByStatus(OrderStatus orderStatus);

}
