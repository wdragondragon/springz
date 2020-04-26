package org.jdragon.springz.test.service;


import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.Service;
import org.jdragon.springz.test.dao.UserDao;
import org.jdragon.springz.test.domain.User;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:10
 * @Description:
 */
@Service
public class UserSaveServiceImpl implements UserSaveService {

    @Autowired
    UserDao userDao;

    @Override
    public void save(User user) {
        System.out.println("==============================");
        System.out.println("UserSaveService===>执行save方法");
        userDao.save(user);
    }
}