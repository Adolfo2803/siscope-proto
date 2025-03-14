package com.nursetrack.service;

import com.nursetrack.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAll();

    Optional<Role> findById(Long id);

    Optional<Role> findByNombre(String nombre);

    Role save(Role role);
}