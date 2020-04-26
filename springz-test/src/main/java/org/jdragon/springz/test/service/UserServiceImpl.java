package org.jdragon.springz.test.service;

import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.Service;
import org.jdragon.springz.test.dao.UserDao;
import org.jdragon.springz.test.domain.User;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 12:15
 * @Description:
 */
@Service("userService")
public class UserServiceImpl implements UserSaveService, UserAddService {

    @Autowired
    UserDao userDao;

    public void addAndSave(User user) {
        save(user);
        add(user);
    }

    @Override
    public void add(User user) {
        System.out.println("==============================");
        System.out.println("UserService===>add:执行add方法");
        userDao.add(user);
    }

    @Override
    public void save(User user) {
        System.out.println("==============================");
        System.out.println("UserService===>save:执行save方法");
        userDao.save(user);
    }
}
