package ua.nix.akolovych.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nix.akolovych.entity.Client;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByEmail(String email);

    boolean existsByEmail(String email);

   Optional<Client> findByLogin(String login);


}
