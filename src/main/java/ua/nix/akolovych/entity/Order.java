package ua.nix.akolovych.entity;


import lombok.*;
import ua.nix.akolovych.enums.OrderStatus;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Client client;

    @Column(name = "date_order")
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "orders_components",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "component_id", referencedColumnName = "id"))
    private Set<Component> components = new HashSet<>();

}
