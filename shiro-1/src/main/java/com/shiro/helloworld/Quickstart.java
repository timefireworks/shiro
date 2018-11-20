package com.shiro.helloworld;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Simple Quickstart application showing how to use Shiro's API.
 *
 * @since 0.9 RC2
 */
public class Quickstart {

    private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);


    public static void main(String[] args) {

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();

        SecurityUtils.setSecurityManager(securityManager);


        //获取当前的Subject,调用SecurityUtils.getSubject()
        Subject currentUser = SecurityUtils.getSubject();

        // Do some stuff with a Session (no need for a web or EJB container!!!)
        //测试使用Session
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("-----------------> Retrieved the correct value! [" + value + "]");
        }

        // let's login the current user so we can check against roles and permissions:
        //测试当前的用户是否已经被认证，即是否已经登录
        if (!currentUser.isAuthenticated()) {
            //把用户名和密码封装为UsernamePasswordToken对象
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            //remember
            token.setRememberMe(true);
            try {
                //执行登录
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("---------------------->There is no user with username of " + token.getPrincipal());
                return;
            } catch (IncorrectCredentialsException ice) {
                log.info("---------------------->Password for account " + token.getPrincipal() + " was incorrect!");
                return;
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            } catch (AuthenticationException ae) {//所有认证时异常的父类
                //unexpected condition?  error?
            }
        }

        //say who they are:
        //print their identifying principal (in this case, a username):
        log.info("------------------->User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role:
        //测试是否有某一个角色
        if (currentUser.hasRole("schwartz")) {
            log.info("-------------------->May the Schwartz be with you!");
        } else {
            log.info("-------------------->Hello, mere mortal.");
            return;
        }

        //test a typed permission (not instance-level)
        //测试用户是否具备某一个行为，调用Subject的isPermitted（）方法
        if (currentUser.isPermitted("lightsaber:weild")) {
            log.info("------------>You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        //a (very powerful) Instance Level permission:
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("-------->You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        //all done - log out!
        //执行登出
        currentUser.logout();

        System.out.println("----->" + currentUser.isAuthenticated());

        System.exit(0);
    }
}
