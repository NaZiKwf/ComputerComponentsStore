package ua.nix.akolovych.security;

public interface SecurityService {
    boolean isAuthenticated();

    boolean autoLogin(String login, String password);

    boolean existsByEmail(String email);

    void logout();
}
