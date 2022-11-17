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
    private String nick_name;
    private int age;
    private String password;
    private Integer privilege;

    public User(Long id, String nick_name, int age, String password) {
        this.nick_name = nick_name;
        this.age = age;
        this.password = password;
    }

    public User(Integer privilege) {
        this.privilege = privilege;
    }
    public User(){

    }
}
