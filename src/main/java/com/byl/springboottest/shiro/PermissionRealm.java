package com.byl.springboottest.shiro;

import com.byl.springboottest.bean.Permission;
import com.byl.springboottest.bean.Role;
import com.byl.springboottest.bean.User;
import com.byl.springboottest.service.PermissionService;
import com.byl.springboottest.service.RoleService;
import com.byl.springboottest.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

public class PermissionRealm extends AuthorizingRealm {

    @Resource
    UserService userService;
    @Resource
    RoleService roleService;
    @Resource
    PermissionService permissionService;

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("进入授权>>");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Role role = roleService.getRoleById(user.getRoleId());
        System.out.println("角色>>"+role.getName());
        simpleAuthorizationInfo.addRole(role.getName());//角色：superadmin,admin,user
        Permission permission = permissionService.getPermissionByRoleId(role.getId());
        System.out.println("权限>>"+permission.getName());
        simpleAuthorizationInfo.addStringPermission(permission.getName());//添加权限
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (StringUtils.isEmpty(authenticationToken.getPrincipal())) {
            return null;
        }
        String username = (String) authenticationToken.getPrincipal();
        //获取用户信息
        User user = userService.getUserByName(username);
        if (user == null) {
            return null;
        } else {
            ByteSource salt = ByteSource.Util.bytes(user.getUsername() + user.getSalt());//参数要与加密时方式一致（用户名+盐值）
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
            clearCachedAuthorizationInfo();
            return simpleAuthenticationInfo;
        }
    }

    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}