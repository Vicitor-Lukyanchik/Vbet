package com.vbet.service.impl;

import com.vbet.entity.Role;

import com.vbet.repository.RoleRepository;
import com.vbet.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
