package cn.mango.mangoblog.runtime;

import cn.mango.mangoblog.utils.TokenUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.User;
import cn.mango.mangoblog.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Primary
@Service
public class UserServiceImpl{
    @Autowired
    private UserMapper userMapper;

    public ResultWrapper<User> getUser(Long id){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("id", id);
        List<User> result = userMapper.selectList(qw);
        if (result.isEmpty()){
            return new ResultWrapper<>(400, "Not found", null);
        }

        return new ResultWrapper<>(result.get(0));
    }

    public ResultWrapper<Boolean> addUser(User user){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("id", user.getId());
        List<User> result = userMapper.selectList(qw);
        if (!result.isEmpty()){
            return new ResultWrapper<>(500, "User already exist", false);
        }

        userMapper.insert(user);

        return new ResultWrapper<>(true);
    }

    public ResultWrapper<Long> registerUser(User user){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("id", user.getId());
        if (userMapper.exists(qw)){
            return new ResultWrapper<>(500, "账号重复", null);
        }
        user.setPrivilege(0);
        userMapper.insert(user);
        //写入数据库后新的id就存在user里

        return new ResultWrapper<>(0, "注册成功", user.getId());
    }

    public ResultWrapper<String> login_check(Long id, String password){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("id", id);
        List<User> users = userMapper.selectList(qw);
        if (users.isEmpty()){
            return new ResultWrapper<>(600, "账号不存在", "");
        }
        User user = users.get(0);
        if (password == null || !Objects.equals(user.getPassword(), password)){
            return new ResultWrapper<>(600, "密码错误", "");
        }

        String token = TokenUtils.gen_token(user);

        return new ResultWrapper<>(0,"Success",token);
    }
    public Boolean UpdateUserPrivilege(Long id,Integer privilege){
        UpdateWrapper<User> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("id",id).set("privilege",privilege);
        return userMapper.update(null,updateWrapper)==1;
    }
}
