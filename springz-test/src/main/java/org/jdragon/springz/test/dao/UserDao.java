package org.jdragon.springz.test.dao;


import org.jdragon.springz.core.annotation.Repository;
import org.jdragon.springz.test.domain.User;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 14:23
 * @Description:
 */
@Repository
public class UserDao {

    public void save(User user) {
        System.out.println("UserDao===>save:" + user.toString());
    }

    public void add(User user) {
        System.out.println("UserDao===>add:" + user.toString());
    }
}
