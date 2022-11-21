package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.LoginMessage;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.User;
import cn.mango.mangoblog.entity.VerifyResult;
import cn.mango.mangoblog.mapper.UserMapper;
import cn.mango.mangoblog.runtime.UserServiceImpl;
import cn.mango.mangoblog.utils.TokenUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;



    @Autowired
    private UserServiceImpl userService;
    @GetMapping("/user/{id}")
    public ResultWrapper<User> getUser(@PathVariable(value = "id")Long id){
        User user=userService.getUser(id);
        if(user==null){
            return new ResultWrapper<>(400,"Not found",null);
        }
        user.setPassword(null);
        return new ResultWrapper<>(user);
    }
    @PostMapping("/me")//用户查看自己的信息
    public ResultWrapper<User> getMySelf(@RequestHeader(value = "authorization", required = true) String token) {
        ResultWrapper<VerifyResult> verifyResultResultWrapper = TokenUtils.Verify(token);
        if (verifyResultResultWrapper.getCode() != 0)
            return new ResultWrapper<>(verifyResultResultWrapper.getCode(), verifyResultResultWrapper.getMessage(), null);
        Long user_id = verifyResultResultWrapper.getData().getId();

        List<User> UserList = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getId, user_id));
        if (UserList.isEmpty()) {
            return new ResultWrapper<>(2, "Invalid token", null);
        }
        User user = UserList.get(0);
        user.setPassword(null);
        return new ResultWrapper<>(0, "Success", user);
    }
    @GetMapping("/users")
    public List<User> getUsers(ModelMap map) {
        return userMapper.selectList(null);
    }
    @GetMapping("/getusers")//根据关键词搜索user
    public ResultWrapper<List<User>> getUsers(@RequestParam(value = "nickname")String nickname){
        List<User> userList=userMapper.GetUserByNickName(nickname);
        if(userList.isEmpty())
            return new ResultWrapper<>(400,"Not found",null);
        else return new ResultWrapper<>(0,"Success",userList);
    }
    @PostMapping("/register")
    public ResultWrapper<Long> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResultWrapper<String> login(@RequestHeader(value = "authorization", required = false) String token, @RequestBody LoginMessage loginMessage) {
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
    public ResultWrapper<Long> UpgradeUser(@RequestParam(value = "id", required = true) Long id, @RequestHeader(value = "authorization", required = true) String token) {
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData == null)
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), null);
        Integer privilege = verifyresultData.getPrivilege();
        if (privilege != 2)
            return new ResultWrapper<>(2, "invalid token", null);
        if (userService.UpdateUserPrivilege(id, 1)) {
            return new ResultWrapper<>(0, "Success", null);
        } else return new ResultWrapper<>(400, "Invalid id", null);
    }
    @PostMapping("/degrade")//实现管理员权限的收回
    public ResultWrapper<Long> DegradeUser(@RequestParam(value = "id", required = true) Long id, @RequestHeader(value = "authorization", required = true) String token) {
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData == null)
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), null);
        Integer privilege = verifyresultData.getPrivilege();
        if (privilege != 2)
            return new ResultWrapper<>(2, "invalid token", null);
        if (userService.UpdateUserPrivilege(id, 0)) {
            return new ResultWrapper<>(0, "Success", null);
        } else return new ResultWrapper<>(400, "Invalid id", null);
    }
    @PostMapping("/profile/edit")//用户修改自己的个人资料
    public ResultWrapper<Boolean> EditUserProfile(@RequestBody User user,@RequestHeader(value = "authorization")String token){
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData == null)
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), false);
        user.setId(verifyresultData.getId());//设置要更新的user的id为token中的id
        if(userService.update_user_profile(user))
            return new ResultWrapper<>(true);
        else return new ResultWrapper<>(500,"数据更新失败",false);
    }
    @PostMapping("/password/edit")//用户修改自己的密码
    public ResultWrapper<Boolean> EditUserPassword(@RequestBody Map<String,String> Map, @RequestHeader(value = "authorization")String token){
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData == null)
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), false);
        if(userService.update_user_password(verifyresultData.getId(), Map.get("new_password"), Map.get("old_password"))){
            return new ResultWrapper<>(true);
        }
        return new ResultWrapper<>(500,"旧密码错误",false);
    }
}
