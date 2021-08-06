package com.byl.springboottest.dao;

import com.byl.springboottest.bean.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    Permission findByRoleId(Integer role_id);

}
