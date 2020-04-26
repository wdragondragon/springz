package org.jdragon.springz.test.service;


import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.Service;
import org.jdragon.springz.test.dao.UserDao;
import org.jdragon.springz.test.domain.User;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 20:44
 * @Description:
 */
@Service
public class UserAddServiceImpl implements UserAddService {

    @Autowired
    UserDao userDao;

    @Override
    public void add(User user) {
        System.out.println("==============================");
        System.out.println("UserAddService===>执行add方法");
        userDao.add(user);
    }
}
