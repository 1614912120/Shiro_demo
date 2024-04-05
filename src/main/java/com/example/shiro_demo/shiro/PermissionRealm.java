package com.example.shiro_demo.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: PermissionRealm
 * Package: com.example.shiro_demo.shiro
 * Description:
 *
 * @Author R
 * @Create 2024/4/5 11:38
 * @Version 1.0
 */

/**
 * 授权就是获取到用户的授权数据
 * 认证根据用户名密码登录 将用户数据保存
 */
public class PermissionRealm extends AuthorizingRealm {


    @Override
    public void setName(String name) {
        super.setName("PermissionRealm");
    }

    /**
         * 授权：授权的主要目的就是查询数据库获取用户的所有角色和权限信息
         */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        List<String> permissions = new ArrayList<String>();
        permissions.add("user:save");
        permissions.add("user:update");

        List<String> roles = new ArrayList<String>();
        roles.add("role1");
        roles.add("role2");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new
                SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);
        simpleAuthorizationInfo.addRoles(roles);
        return simpleAuthorizationInfo;
    }

    /**
         * 认证：认证的主要目的，比较用户输入的用户名密码是否和数据库中的一致
     将安全数据存到shiro保管
         */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取登录的upToken
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        //2.获取输入的用户名密码
        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //3.根据用户名查询数据库
        if(!password.equals("123456")) {
            throw new RuntimeException("用户名或密码错误");//抛出异常表示认证失败
        }
        //4.比较密码和数据库中的密码是否一致 密码可能加密
        //5.如果成功 像shiro存入安全数据
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, this.getName());
        return  info;
    }
}
