package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.LoginMessage;
import cn.mango.mangoblog.entity.VerifyResult;
import cn.mango.mangoblog.runtime.UserServiceImpl;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.User;
import cn.mango.mangoblog.mapper.UserMapper;
import cn.mango.mangoblog.utils.TokenUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/users")
    public List<User> getUsers(ModelMap map) {
        return userMapper.selectList(null);
    }

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public ResultWrapper<Long> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResultWrapper<String> login(@RequestParam(value = "authorization", required = false) String token, @RequestBody LoginMessage loginMessage) {
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData != null) {
            Long id = verifyresultData.getId();
            if (id == loginMessage.id) {
                return new ResultWrapper<>(500, "重复登录", token);
            }
        }
            return userService.login_check(loginMessage.id, loginMessage.password);
    }

    @PostMapping("/upgrade")
    public ResultWrapper<Long> UpgradeUser(@RequestParam(value = "id",required = true)Long id,@RequestHeader(value = "authorization",required = true)String token){
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData == null)
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), null);
        Integer privilege = verifyresultData.getPrivilege();
        if(privilege!=2)
            return new ResultWrapper<>(2,"invalid token",0L);
        if(userService.UpdateUserPrivilege(id,1)){
            return new ResultWrapper<>(0,"Success",null);
        }
        else return new ResultWrapper<>(400,"Invalid id",0L);
    }
}
