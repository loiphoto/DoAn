package com.example.doan.Service;

import com.example.doan.Entity.Role;
import com.example.doan.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public List<Role> findAll(){
        return roleRepository.findAll();
    }
}
