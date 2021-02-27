package org.jdragon.springz.test.service;

import org.jdragon.springz.core.annotation.Inject;
import org.jdragon.springz.core.annotation.Service;
import org.jdragon.springz.test.dao.UserDao;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 12:15
 * @Description:
 */
@Service("userService")
public class UserServiceImpl implements UserSaveService, UserAddService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    UserDao userDao;

    public void addAndSave(User user) {
        save(user);
        add(user);
    }

    @Override
    public void add(User user) {
        logger.trace("UserService","add:执行add方法");
        userDao.add(user);
    }

    @Override
    public void test() {

    }

    @Override
    public void save(User user) {
        logger.trace("UserService","save:执行save方法");
        userDao.save(user);
    }
}
