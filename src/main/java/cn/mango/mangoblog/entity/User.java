package cn.mango.mangoblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String nickname;
    private int age;
    private String password;
    private Integer privilege;

    public User(Long id, String nickname, int age, String password) {
        this.nickname = nickname;
        this.age = age;
        this.password = password;
    }

    public User(Integer privilege) {
        this.privilege = privilege;
    }
    public User(){

    }

    public Long getId() {
        return id;
    }
}
