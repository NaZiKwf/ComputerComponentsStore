package ua.nix.akolovych.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

        List<Order> findAllByClientId(UUID id);

        void deleteById(UUID id);

        List<Order> findAllByStatus(OrderStatus orderStatus);

        @Modifying
        @Query(nativeQuery = true, value = "DELETE from orders_components WHERE" +
               " orders_components.order_id = ?1 ")
        void deleteAllComponentsFromOrder(UUID orderId);
}
