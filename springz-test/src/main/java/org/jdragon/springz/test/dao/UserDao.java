package org.jdragon.springz.test.dao;


import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.Repository;
import org.jdragon.springz.core.annotation.Resource;
import org.jdragon.springz.test.domain.User;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 14:23
 * @Description:
 */
@Repository
public class UserDao {

    User user1;

    @Autowired
    public void setUser1(User user1){
        this.user1 = user1;
    }

    public void save(User user) {
        System.out.println("UserDao===>save:" + user.toString());
    }

    public void add(User user) {
        System.out.println("UserDao===>add:" + user.toString());
    }
}
