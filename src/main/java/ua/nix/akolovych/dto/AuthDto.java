package ua.nix.akolovych.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthDto {
    String login;
    String password;
}
