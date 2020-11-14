package org.jdragon.springz.test.config;



import org.jdragon.springz.ImportTest;
import org.jdragon.springz.core.annotation.*;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.test.controller.UserController;
import org.jdragon.springz.test.domain.Car;
import org.jdragon.springz.test.domain.TestWaitBean;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.test.service.feign.HttpTest;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 02:10
 * @Description:
 */
@Configuration
@Import(ImportTest.class)
public class CreateBean {

    @Value("张三")
    private String username;

    @Value("123456")
    private String password;

    @Value("137*******")
    private String tel;

    @Value("2000-02-10")
    private String birth;

    @Value("true")
    private Boolean vip;

    @Bean
    @Scope(BeanInfo.PROTOTYPE)
    public User user(UserController controller) {
        return new User(username, password, tel, birth, vip);
    }

    @Bean("carOne")
    public Car car(){
        return new Car(username);
    }

    //循环依赖
    @Bean("httpUser")
    public User user(TestWaitBean testWaitBean){
        testWaitBean.print();
        return new User(username, password, tel, birth, vip);
    }
    @Bean
    public TestWaitBean testWaitBean(HttpTest httpTest){
        httpTest.http();
        return new TestWaitBean();
    }
}
