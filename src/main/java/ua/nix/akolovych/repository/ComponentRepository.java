package ua.nix.akolovych.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.nix.akolovych.entity.Component;

import java.util.List;
import java.util.UUID;

import static org.hibernate.sql.ast.Clause.SELECT;
import static org.hibernate.sql.ast.Clause.WHERE;

@Repository
public interface ComponentRepository extends JpaRepository<Component, UUID> {

    boolean existsByName(String name);

    @Query(nativeQuery = true, value = "SELECT components.* FROM components " +
            "INNER JOIN orders_components " +
            "ON components.id = orders_components.component_id " +
            "WHERE orders_components.order_id = ?1 ")
    List<Component> findAllByOrderId(UUID orderId);

    @Query(nativeQuery = true, value = "SELECT components.* FROM components " +
            "INNER JOIN favourities " +
            "ON components.id = favourities.component_id " +
            "WHERE favourities.user_id = ?1 ")
    List<Component> findAllUserFavouritesComponents(UUID userId);
}
