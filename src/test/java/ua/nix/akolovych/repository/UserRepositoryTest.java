package ua.nix.akolovych.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nix.akolovych.entity.Client;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;

   @Test
    public void findByEmail_shouldReturnPresentOptionalOfClientEntity(){
        Client client = new Client(null, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());

        userRepository.save(client);

        Optional<Client> actual = userRepository.findByEmail(client.getEmail());

        assertTrue(actual.isPresent());
    }

    @Test
    public void findByLogin_shouldReturnPresentOptionalOfClientEntity(){
        Client client = new Client(null, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());

        userRepository.save(client);

        Optional<Client> actual = userRepository.findByLogin(client.getLogin());

        assertTrue(actual.isPresent());
    }

    @Test
    public void existByEmail_shouldReturnTrueOrFalse(){
        Client client = new Client(null, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());

        userRepository.save(client);

        boolean actual = userRepository.existsByEmail(client.getEmail());

        assertTrue(actual);
    }
}
