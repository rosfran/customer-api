package com.hr.management.service;

import com.hr.management.dto.mapper.UserRequestMapper;
import com.hr.management.dto.request.LoginRequest;
import com.hr.management.dto.request.UserRequest;
import com.hr.management.dto.response.CommandResponse;
import com.hr.management.repository.UserRepository;
import com.hr.management.security.UserDetailsImpl;
import com.hr.management.dto.response.JwtResponse;
import com.hr.management.exception.ElementAlreadyExistsException;
import com.hr.management.model.Role;
import com.hr.management.model.RoleType;
import com.hr.management.model.User;
import com.hr.management.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.hr.management.common.Constants.ALREADY_EXISTS_USER;
import static com.hr.management.common.Constants.CREATED_USER;

/**
 * Service used for Authentication related operations
 */
@Slf4j(topic = "AuthService")
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserRequestMapper userRequestMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    /**
     * Authenticates users by their credentials
     *
     * @param request
     * @return JwtResponse
     */
    public JwtResponse login(LoginRequest request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername().trim(), request.getPassword().trim()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return JwtResponse.builder().token(jwt).id(userDetails.getId()).username(userDetails.getUsername()).roles(roles).build();
    }

    /**
     * Registers users using their credentials and user info
     *
     * @param request
     * @return id of the registered user
     */
    public CommandResponse signup(UserRequest request) {
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername().trim()))
            throw new ElementAlreadyExistsException(ALREADY_EXISTS_USER);

        final User user = userRequestMapper.toEntity(request);
        user.setPassword(encoder.encode(request.getPassword().trim()));
        // add default role to the user
        user.setRoles(new HashSet<>(List.of(new Role(1L, RoleType.ROLE_USER))));
        userRepository.save(user);
        log.info(CREATED_USER);
        return CommandResponse.builder().id(user.getId()).build();
    }
}
