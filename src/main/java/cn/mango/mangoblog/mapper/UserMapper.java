package cn.mango.mangoblog.mapper;

import cn.mango.mangoblog.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import feign.QueryMap;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {
    List<User> GetUserByNickName(String nick_name);
}
