package com.shiro.realms;

import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

/**
 * author: San Jinhong
 * date: 2018/11/19 10:20
 **/
public class ShiroRealm2 extends AuthenticatingRealm {
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.把AuthenticationToken转化为UsernamePasswordToken
        UsernamePasswordToken upToken = (UsernamePasswordToken)token;

        //2.从UsernamePasswordToken中获取username
        String username = upToken.getUsername();

        //3.调用数据库的方法，从数据库查询username对应的用户记录
        System.out.println("从数据库中获取username: " + username + " 所对应的用户信息");

        //4.若用户不存在，则可以抛出UnknownAccountException异常
        if( "unknown".equals(username)){
            throw new UnknownAccountException("用户不存在");
        }

        //5.根据用户信息的情况，决定是否需要抛出其他的AuthenticationException异常
        if("monster".equals(username)){
            throw new LockedAccountException("用户被锁定");
        }

        //6.根据用户的情况，来构建AuthenticationInfo对象并返回,通常的实现类是：SimpleAuthenticationInfo
        //以下信息是从数据库中获取的
        //1）principal: 认证的实体信息，可以是username, 也可以是数据库表对应的用户的实体类对象
        Object principal = username;

        //2）credentials: 密码
        Object credentials = null;
        if("admin".equals(username)){
            credentials = "";
        } else if ("user".equals(username)){
            credentials = ""
        }

        //3)盐值
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);

        //4)//realmNamm: 当前realm对象的name,调用符类的getName()方法即可
        String realmName = getName();

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, realmName);

        return info;
    }

    //MD5加密"123456" 1024次
    public static void main(String[] args) {
        String hashAlgorithmName = "MD5";
        Object credentials = "123456";
        Object salt = null;
        int hashIterations = 1024;

        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(result);
    }
}
