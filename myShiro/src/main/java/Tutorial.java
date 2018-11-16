import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * author: San Jinhong
 * date: 2018/11/15 17:07
 **/
public class Tutorial {

    public static void main(String[] args) {

        System.out.println("My First Apache Shiro Application");

        //1.需要导入import org.apache.shiro.mgt.SecurityManager;否则java.lang.SecurityManager代码报错
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        //2.
        SecurityManager securityManager = factory.getInstance();

        //3.
        SecurityUtils.setSecurityManager(securityManager);

        //4、创建用户
        Subject currentUser = SecurityUtils.getSubject();

        //5、创建session
        Session session = currentUser.getSession();
        session.setAttribute("name", "sssss");


        if(!currentUser.isAuthenticated()){

            System.out.println(currentUser.isAuthenticated());

            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr1","vespa");
            System.out.println(currentUser.isAuthenticated());

            System.out.println("token:" + token);

            token.setRememberMe(true);
            System.out.println(currentUser.isAuthenticated());

            try {
                currentUser.login( token );
                System.out.println(currentUser.isAuthenticated());
                //if no exception, that's it, we're done!
            } catch ( UnknownAccountException uae ) {
                System.out.println(uae.getMessage());
                //username wasn't in the system, show them an error message?
            } catch ( IncorrectCredentialsException ice ) {
                System.out.println(ice.getMessage());
                //password didn't match, try again?
            } catch ( LockedAccountException lae ) {
                System.out.println(lae.getMessage());
                //account for that username is locked - can't login.  Show them a message?
            } catch ( AuthenticationException ae ) {
                System.out.println(ae.getMessage());
            //unexpected condition - error?
            }
        }


        System.exit(0);
    }
}
