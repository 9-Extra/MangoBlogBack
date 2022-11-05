package cn.mango.mangoblog.runtime;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.User;
import cn.mango.mangoblog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ResultWrapper<Boolean> registerUser(User user){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("id", user.getId());
        if (userMapper.exists(qw)){
            return new ResultWrapper<>(500, "Id already exist", false);
        }
        userMapper.insert(user);

        return new ResultWrapper<>(true);
    }
}
