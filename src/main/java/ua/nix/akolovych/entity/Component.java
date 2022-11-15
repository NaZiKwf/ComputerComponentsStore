package ua.nix.akolovych.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "components")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Component extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "description")
    private String description;

    @Column(name = "specifications")
    private String specifications;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE},fetch = FetchType.EAGER,mappedBy = "components")
    private Set<Order> orders = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "favourities",
            joinColumns = @JoinColumn(name = "component_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<Client> clients = new HashSet<>();

}
