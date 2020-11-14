package org.jdragon.springz.test.service;



import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Service;
import org.jdragon.springz.test.dao.UserDao;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 20:44
 * @Description:
 */
@Service
public class UserAddServiceImpl implements UserAddService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @AutowiredZ
    UserDao userDao;

    @Override
    public void add(User user) {
        logger.trace("UserAddService","执行add方法");
        userDao.add(user);
    }

    @Override
    @PostConstruct
    public void test(){
        logger.trace("UserAddService","执行test方法");
    }
}
