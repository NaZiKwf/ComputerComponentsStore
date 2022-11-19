package ua.nix.akolovych.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.nix.akolovych.entity.Client;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;

    private String firstName;

    private String lastName;

    private String phone;

    @NotEmpty(message = "Email can't be empty!")
    @Email(message = "{Email.RegistrationDto.Email}")
    private String email;

    private String address;

    private String login;

    private String password;

    public UserDto(Client client){
        this.id = client.getId();
        this.firstName= client.getFirstName();
        this.lastName= client.getLastName();
        this.phone= client.getPhone();
        this.email= client.getEmail();
        this.address= client.getAddress();
        this.login= client.getLogin();
        this.password= client.getPassword();
    }
}
