package ua.nix.akolovych.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {

    @NotEmpty(message = "Login can't be empty!")
    @Size(min=5, message = "{Size.RegistrationDto.Login}")
    String login;


    @NotEmpty(message = "Password can't be empty!")
    @Size(min=5, message = "{Size.RegistrationDto.Password}")
    String password;

    @NotEmpty(message = "Email can't be empty!")
    @Email(message = "{Email.RegistrationDto.Email}")
    String email;
}
