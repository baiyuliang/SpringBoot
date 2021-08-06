package com.byl.springboottest.service;

import com.byl.springboottest.bean.Permission;
import com.byl.springboottest.dao.PermissionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    PermissionRepository permissionRepository;

    @Override
    public Permission getPermissionByRoleId(Integer role_id) {
        return permissionRepository.findByRoleId(role_id);
    }

}
