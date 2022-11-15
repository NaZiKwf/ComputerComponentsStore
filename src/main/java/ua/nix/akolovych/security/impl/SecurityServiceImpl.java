package ua.nix.akolovych.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ua.nix.akolovych.repository.UserRepository;
import ua.nix.akolovych.security.SecurityService;

import java.util.Objects;

@Service
public class SecurityServiceImpl  implements SecurityService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    @Autowired
    public SecurityServiceImpl(AuthenticationManager authenticationManager,
                               UserDetailsService userDetailsService,
                               UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }


    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.isNull(authentication) || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())){
            return false;
        }
        return authentication.isAuthenticated();
    }

    @Override
    public boolean autoLogin(String login, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                login,
                password,
                userDetails.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void logout() {
        if(Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())){
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }


}
