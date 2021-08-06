package com.byl.springboottest.service;

import com.byl.springboottest.bean.Permission;

public interface PermissionService{

    Permission getPermissionByRoleId(Integer role_id);
}
