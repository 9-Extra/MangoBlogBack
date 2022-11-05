package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.runtime.UserServiceImpl;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.User;
import cn.mango.mangoblog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/users")
    public List<User> getUsers(ModelMap map){
        return userMapper.selectList(null);
    }

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public ResultWrapper<Boolean> registerUser(@RequestBody User user){
        return userService.registerUser(user);
    }
}
