package com.byl.springboottest.service;

import com.byl.springboottest.bean.Role;
import com.byl.springboottest.dao.RoleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    RoleRepository roleRepository;

    @Override
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id).get();
    }


}
