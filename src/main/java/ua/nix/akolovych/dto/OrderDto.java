package ua.nix.akolovych.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.enums.OrderStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private UUID id;

    private String userLogin;

    private UUID userId;

    private Instant date;

    private OrderStatus status;

    public OrderDto(Order order){
        this.id = order.getId();
        this.userLogin = order.getClient().getLogin();
        this.userId= order.getClient().getId();
        this.date = order.getDate();
        this.status=order.getStatus();
    }

}
