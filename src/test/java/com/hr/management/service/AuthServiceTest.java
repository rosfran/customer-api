package com.hr.management.service;

import com.hr.management.dto.mapper.UserRequestMapper;
import com.hr.management.dto.request.LoginRequest;
import com.hr.management.dto.request.UserRequest;
import com.hr.management.dto.response.CommandResponse;
import com.hr.management.dto.response.JwtResponse;
import com.hr.management.model.Role;
import com.hr.management.model.RoleType;
import com.hr.management.model.User;
import com.hr.management.repository.UserRepository;
import com.hr.management.exception.ElementAlreadyExistsException;
import com.hr.management.security.JwtUtils;
import com.hr.management.security.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for AuthService methods
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRequestMapper userRequestMapper;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    /**
     * Method under test: {@link AuthService#login(LoginRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/auth.csv")
    void login_should_returnJwtResponse_when_UserAuthenticated(long id, String username, String password, String credentials, String role, String token) throws AuthenticationException {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        List<GrantedAuthority> roles = Collections.singletonList(grantedAuthority);

        when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken(
                new UserDetailsImpl(id, username, password, roles), credentials));
        when(jwtUtils.generateJwtToken(any())).thenReturn(token);

        JwtResponse result = authService.login(request);

        assertEquals(id, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(token, result.getToken());
        assertEquals(role, result.getRoles().get(0));
        verify(jwtUtils).generateJwtToken(any());
        verify(authenticationManager).authenticate(any());
    }

    /**
     * Method under test: {@link AuthService#signup(UserRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/auth.csv")
    void signup_should_returnCommandResponse_when_UserSignup(long id, String username, String password, String credentials, String role, String token) throws AuthenticationException {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);

        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setPassword(password);

        Set<Role> roles = new HashSet<>(List.of(new Role(1L, RoleType.ROLE_USER)));

        when(userRepository.existsByUsernameIgnoreCase(username)).thenReturn(false);
        when(userRequestMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn(password);

        CommandResponse result = authService.signup(request);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals(capturedUser.getId(), result.id());
        assertEquals(username, capturedUser.getUsername());
        assertEquals(password, capturedUser.getPassword());
        assertEquals(roles, capturedUser.getRoles());
    }

    /**
     * Method under test: {@link AuthService#signup(UserRequest)}
     */
    @Test
    void signup_should_throwElementAlreadyExistsException_when_UserAlreadyExists() {
        String username = "username";
        UserRequest request = new UserRequest();
        request.setUsername(username);

        when(userRepository.existsByUsernameIgnoreCase(username)).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> authService.signup(request));

        verify(userRepository, never()).save(any());
    }
}
