package org.jdragon.springz.test.controller;


import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Controller;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.test.service.UserAddService;
import org.jdragon.springz.test.service.UserSaveService;
import org.jdragon.springz.test.service.UserServiceImpl;
import org.jdragon.springz.utils.http.response.Result;
import org.jdragon.springz.web.annotation.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:08
 * @Description:
 */

@RestController
//    @Controller
@RequestMapping("/user")
public class UserController {

    @AutowiredZ
    UserSaveService saveService;

    @AutowiredZ
    UserAddService addService;

    @AutowiredZ
    @Qualifier("userService")
    UserSaveService userSaveService;

    @AutowiredZ
    @Qualifier("userService")
    UserAddService userAddService;

    @AutowiredZ
    @Qualifier("userService")
    UserServiceImpl userService;

    @GetMapping("/save")
    public void save(User user) {
        saveService.save(user);
    }

    @GetMapping("/add")
    public void add(@RequestBody User user) {
        addService.add(user);
    }

    @GetMapping("/add/{test}")
    public Result<String> add(@PathVariable("test") String test) {
        System.out.println("控制器add:" + test);
        return Result.success("控制器add:" + test);
    }

    @GetMapping("/{add}/test")
    public Result<String> add2(@PathVariable("add") String add) {
        System.out.println("控制器add2:" + add);
        return Result.success("控制器add2:" + add);
    }

    public void saveAndAdd(User user) {
        userSaveService.save(user);
        userAddService.add(user);
        userService.addAndSave(user);
    }
}
