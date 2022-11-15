package ua.nix.akolovych.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.nix.akolovych.dto.UserDto;
import ua.nix.akolovych.entity.Client;
import ua.nix.akolovych.entity.Component;
import ua.nix.akolovych.entity.Order;
import ua.nix.akolovych.entity.Role;
import ua.nix.akolovych.exception.EntityNotFoundException;
import ua.nix.akolovych.repository.ComponentRepository;
import ua.nix.akolovych.repository.OrderRepository;
import ua.nix.akolovych.repository.RoleRepository;
import ua.nix.akolovych.repository.UserRepository;
import ua.nix.akolovych.service.UserService;
import ua.nix.akolovych.utils.ConvertorForEntityAndDto;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final RoleRepository roleRepository;

    private final ComponentRepository componentRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, OrderRepository orderRepository, RoleRepository roleRepository, ComponentRepository componentRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.roleRepository = roleRepository;
        this.componentRepository = componentRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Client getById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<Client> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Client create(UserDto userDto) {
        Client client = new Client();
        client.setId(null);
        client.setEmail(userDto.getEmail());
        client.setLogin(userDto.getLogin());
        client.setPassword( bCryptPasswordEncoder.encode(userDto.getPassword()));
        Role baseRole = roleRepository.findByName("ROLE_USER").orElse(null);
        if(Objects.isNull(baseRole)){
            baseRole = new Role(null,"ROLE_USER", new HashSet<>());
            roleRepository.save(baseRole);
        }
        userRepository.save(client);
        baseRole.getClients().add(client);
        roleRepository.save(baseRole);
        client.getRoles().add(baseRole);
        return userRepository.save(client);
    }

    @Override
    public Client update(UserDto userDto) {
        if(Objects.isNull(getById(userDto.getId()))){
            log.warn("Client not found");
            throw new EntityNotFoundException("not found ...");
        }
        return userRepository.save(ConvertorForEntityAndDto.userDtoToEntity(userDto));
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public Client findByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    @Override
    public List<Order> findAllUserOrders(UUID userId) {
        return orderRepository.findAllByClientId(userId);
    }

    @Override
    public void addComponentToFavourites(UUID componentId, UUID userId) {
        Client client = getById(userId);
        if(Objects.nonNull(client)){
            Component component = componentRepository.findById(componentId).orElse(null);
            if(Objects.nonNull(component)){
                client.getComponents().add(component);
                component.getClients().add(client);
                userRepository.save(client);
                componentRepository.save(component);
            }

        }
    }

    @Override
    public void deleteComponentFromFavourites(UUID componentId, UUID userId) {
        Client client = getById(userId);
        if(Objects.nonNull(client)){
            Component component = componentRepository.findById(componentId).orElse(null);
            if(Objects.nonNull(component)){
                client.getComponents().remove(component);
                component.getClients().remove(client);
                userRepository.save(client);
                componentRepository.save(component);
            }

        }
    }
}
