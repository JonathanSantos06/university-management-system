package mx.edu.sgu.auth.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.auth.domain.Role;
import mx.edu.sgu.auth.dto.response.RoleResponse;
import mx.edu.sgu.auth.exception.ResourceNotFoundException;
import mx.edu.sgu.auth.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream().map(RoleResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + name));
    }
}
