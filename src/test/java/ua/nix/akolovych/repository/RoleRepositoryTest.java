package ua.nix.akolovych.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Role;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void findByName_shouldReturnRoleEntity(){

        Role role = new Role(null,"ROLE_USER", new HashSet<>());

        Optional<Role> actual = roleRepository.findByName(role.getName());

        assertTrue(actual.isPresent());
    }
}
