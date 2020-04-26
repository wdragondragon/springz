package org.jdragon.springz.test.config;


import org.jdragon.springz.core.annotation.Bean;
import org.jdragon.springz.core.annotation.Configuration;
import org.jdragon.springz.core.annotation.Value;
import org.jdragon.springz.test.domain.User;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 02:10
 * @Description:
 */
@Configuration
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
//    @Scope(BeanInfo.PROTOTYPE)
    public User user() {
        return new User(username, password, tel, birth, vip);
    }
}
