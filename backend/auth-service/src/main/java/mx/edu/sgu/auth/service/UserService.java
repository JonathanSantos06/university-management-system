package mx.edu.sgu.auth.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.auth.domain.Role;
import mx.edu.sgu.auth.domain.User;
import mx.edu.sgu.auth.dto.request.AssignRolesRequest;
import mx.edu.sgu.auth.dto.request.ChangePasswordRequest;
import mx.edu.sgu.auth.dto.request.CreateUserRequest;
import mx.edu.sgu.auth.dto.response.UserResponse;
import mx.edu.sgu.auth.exception.DuplicateResourceException;
import mx.edu.sgu.auth.exception.InvalidCredentialsException;
import mx.edu.sgu.auth.exception.ResourceNotFoundException;
import mx.edu.sgu.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        return UserResponse.from(getUserOrThrow(id));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("El nombre de usuario ya existe: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("El correo ya está registrado: " + request.email());
        }

        Set<Role> roles = request.roles().stream().map(roleService::getByName).collect(Collectors.toSet());

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .active(true)
                .locked(false)
                .roles(roles)
                .build();

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse assignRoles(UUID id, AssignRolesRequest request) {
        User user = getUserOrThrow(id);
        Set<Role> roles = request.roles().stream().map(roleService::getByName).collect(Collectors.toSet());
        user.setRoles(roles);
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse setActive(UUID id, boolean active) {
        User user = getUserOrThrow(id);
        user.setActive(active);
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse setLocked(UUID id, boolean locked) {
        User user = getUserOrThrow(id);
        user.setLocked(locked);
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void changePassword(UUID id, ChangePasswordRequest request) {
        User user = getUserOrThrow(id);
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("La contraseña actual no es correcta");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private User getUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
    }
}
