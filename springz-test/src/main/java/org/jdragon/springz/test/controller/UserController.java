package org.jdragon.springz.test.controller;


import org.jdragon.springz.core.annotation.Inject;
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
@RequestMapping("/user")
public class UserController {

    @Inject
    UserSaveService saveService;

    @Inject
    UserAddService addService;

    @Inject("userService")
    UserSaveService userSaveService;

    @Inject("userService")
    UserAddService userAddService;

    @Inject("userService")
    UserServiceImpl userService;

    @GetMapping("/save")
    public void save(User user) {
        saveService.save(user);
    }

    @PostMapping("/add")
    public void add(@RequestBody User user) {
        addService.add(user);
    }

    @GetMapping("/add/{str}")
    public Result<String> add(@PathVariable String str) {
        return Result.success("控制器add:" + str);
    }

    @GetMapping("/add/test")
    public Result<String> add2(@RequestParam String add) {
        return Result.success("控制器add2:" + add);
    }

    @PostMapping("/header/test/{path}")
    public Result<String> header(@RequestParam String add, @PathVariable String path, @RequestHeader String header, @RequestBody User user) {
        return Result.success("控制器add:" + add + " path:" + path + " header:" + header + " user:" + user);
    }

    @GetMapping("/get")
    public Result<User> get() {
        return Result.success(new User());
    }

    public void saveAndAdd(User user) {
        userSaveService.save(user);
        userAddService.add(user);
        userService.addAndSave(user);
    }
}
