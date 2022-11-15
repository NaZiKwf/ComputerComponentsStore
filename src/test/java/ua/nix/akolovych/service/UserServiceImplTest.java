package ua.nix.akolovych.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.nix.akolovych.dto.UserDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Component;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.entity.Role;
import ua.nix.akolovych.enums.OrderStatus;
import ua.nix.akolovych.repository.ComponentRepository;
import ua.nix.akolovych.repository.OrderRepository;
import ua.nix.akolovych.repository.RoleRepository;
import ua.nix.akolovych.repository.UserRepository;
import ua.nix.akolovych.service.impl.UserServiceImpl;
import ua.nix.akolovych.utils.ConvertorForEntityAndDto;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ComponentRepository componentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void getById_return_clientId() {
        UUID id = UUID.randomUUID();

        Client client = new Client(id, "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());


        given(userRepository.findById(id)).willReturn(Optional.of(client));

        final Client expected = userService.getById(id);

        Assertions.assertThat(expected).isNotNull();
    }

    @Test
    void getAll_should_returnAllUsers(){
        List<Client> users = new ArrayList<>();
        users.add(new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>()));
        users.add (new Client(UUID.randomUUID(), "Pavlo", "Dyachenko", "4a636363652", "pablo@gmail.com","...",
                "pavlo123", "pavlo321",new HashSet<>(),new HashSet<>(),new HashSet<>()));
        users.add (new Client(UUID.randomUUID(), "Petro", "Syva", "42425252", "petya@gmail.com","...",
                "petro123", "petro321",new HashSet<>(),new HashSet<>(),new HashSet<>()));

        given(userRepository.findAll()).willReturn(users);

        List<Client> expected = userService.getAll();

        assertEquals(expected, users);
    }

    @Test
    void findByLogin_should_returnUClient(){
        String login = "123";

        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                login, "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());


        given(userRepository.findByLogin(login)).willReturn(Optional.of(client));

        final Client expected = userService.findByLogin(login);

        Assertions.assertThat(expected).isNotNull();
    }

    @Test
    void delete_should_removeUser(){
        UUID id = UUID.randomUUID();

        userService.delete(id);
        userService.delete(id);

        verify(userRepository,times(2)).deleteById(id);
    }

    @Test
    void update_should_returnClient(){
        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan132", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        given(userRepository.save(any(Client.class))).willReturn(client);
        given(userRepository.findById(client.getId())).willReturn(Optional.of(client));
        Client actual = userService.update(ConvertorForEntityAndDto.userEntityToDto(client));

        Assertions.assertThat(actual).isNotNull();

        verify(userRepository).save(any(Client.class));

    }

    @Test
    void findAllUserOrders_should_returnClientOrders(){
        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "ivan123", "ivan321",new HashSet<>(),new HashSet<>(),new HashSet<>());
        Order order = new Order(UUID.randomUUID(),client,Instant.now(), OrderStatus.UNFINISHED, new HashSet<>());
        client.getOrders().add(new Order(UUID.randomUUID(),null, Instant.now(),null,new HashSet<>()));
        given(orderRepository.findAllByClientId(client.getId())).willReturn(List.of(order));
        List<Order> actual = userService.findAllUserOrders(client.getId());

        Assertions.assertThat(actual).hasSize(1);
    }

    @Test
    void create_should_returnUser(){
        Role role = new Role();
        Client client = new Client(UUID.randomUUID(), "Ivan", "Ivanov", "42425252", "ivanov@gmail.com","...",
                "23232", "ivan321",new HashSet<>(),new HashSet<>(Set.of(role)),new HashSet<>());
        UserDto userDto = ConvertorForEntityAndDto.userEntityToDto(client);
        given(bCryptPasswordEncoder.encode(any(String.class))).willReturn("1");
        given(roleRepository.findByName("ROLE_USER")).willReturn(Optional.of(role));
        given(roleRepository.save(role)).willReturn(role);
        given(roleRepository.save(role)).willReturn(role);
        given(userRepository.save(any(Client.class))).willReturn(client);

        userService.create(userDto);

        verify(userRepository,times(2)).save(any(Client.class));
    }

}
