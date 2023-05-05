package org.jdragon.springz.test.dao;


import org.jdragon.springz.core.annotation.Repository;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 14:23
 * @Description:
 */
@Repository
public class UserDao {

    Logger logger = LoggerFactory.getLogger(getClass());

    public void save(User user) {
        logger.trace("UserDao", "save:" + user.toString());
    }

    public void add(User user) {
        logger.trace("UserDao", "add:" + user.toString());
    }
}
