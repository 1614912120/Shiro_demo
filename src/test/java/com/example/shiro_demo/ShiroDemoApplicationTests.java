package com.example.shiro_demo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShiroDemoApplicationTests {

    /**
     * 测试用户验证
     * 1.根据配置文件创建factory
     * 2.通过工厂获取获取securityManager
     * 3.将securityManager绑定到当前运行环境
     * 4.从当前环境构造subject
     * 5.构造shiro登录数据
     * 6.主题登录
     */
    @Test
    void contextLoads() {
        //1.加载ini配置文件创建SecurityManager
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro-test-1.ini");
        //2.获取securityManager
        SecurityManager securityManager = factory.getInstance();
        //3.将securityManager绑定到当前运行环境
        /**
         * 这一行负责将前面创建的SecurityManager实例绑定到当前的JVM运行环境中。一旦这个绑定完成，你就可以在程序的其他任何地方，
         * 通过SecurityUtils.getSubject()获取当前用户（Subject）的信息，并执行相关的安全操作，比如登录、登出、检查权限等。
         */
        SecurityUtils.setSecurityManager(securityManager);
        //3.将securityManager绑定到当前运行环境
        Subject subject = SecurityUtils.getSubject();
        //5.构造主体登录的凭证（即用户名/密码）
        UsernamePasswordToken upToken = new UsernamePasswordToken("zhangsan", "123456");
        subject.login(upToken);
        System.out.println("用户登录成功="+subject.isAuthenticated());
        //getPrincipal 获取登录成功的安全数据
        System.out.println(subject.getPrincipal());
    }



    @Test
    public void test2() {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro-test-2.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken upToken = new UsernamePasswordToken("zhangsan", "123456");
        subject.login(upToken);
        System.out.println("用户登录成功="+subject.isAuthenticated());
        /**
         * 登录成功后完成授权
         * 授权：检验当前登录用户是否具有操作权限 是否具有某个角色
         */
        boolean hasPerm = subject.isPermitted("user:save");
        System.out.println("用户是否具有save权限="+hasPerm);
    }

    @Test
    public void test3() {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken upToken = new UsernamePasswordToken("lisi", "123456");
        subject.login(upToken);
        System.out.println("当前用户具有user:save权限="+subject.isPermitted("user:save"));
    }

}
