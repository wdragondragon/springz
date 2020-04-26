package org.jdragon.springz.test.controller;


import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.Controller;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.test.service.UserAddService;
import org.jdragon.springz.test.service.UserSaveService;
import org.jdragon.springz.test.service.UserServiceImpl;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:08
 * @Description:
 */

@Controller
public class UserController {

    @Autowired
    UserSaveService saveService;

    @Autowired
    UserAddService addService;

    @Autowired
    @Qualifier("userService")
    UserSaveService userSaveService;

    @Autowired
    @Qualifier("userService")
    UserAddService userAddService;

    @Autowired
    @Qualifier("userService")
    UserServiceImpl userService;

    public void save(User user) {
        saveService.save(user);
    }

    public void add(User user) {
        addService.add(user);
    }

    public void saveAndAdd(User user) {
        userSaveService.save(user);
        userAddService.add(user);

        userService.addAndSave(user);
    }
}
