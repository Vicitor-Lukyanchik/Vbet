package com.vbet.service;

import com.vbet.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> findByName(String name);
}
