package org.jdragon.springz.test.service;



import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Service;
import org.jdragon.springz.test.dao.UserDao;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:10
 * @Description:
 */
@Service
public class UserSaveServiceImpl implements UserSaveService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @AutowiredZ
    UserDao userDao;

    @Override
    public void save(User user) {
        logger.trace("UserSaveService","执行save方法");
        userDao.save(user);
    }
}