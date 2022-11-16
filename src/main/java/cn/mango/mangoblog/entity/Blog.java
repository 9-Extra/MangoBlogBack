package cn.mango.mangoblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jdk.jfr.Timestamp;
import lombok.Data;

@Data
@TableName(value = "blogs")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long author_id;
    private Integer status;
    //0 new&&!save
    //1 save
    //2 publish

    public Blog(Long author_id, Integer status) {
        this.author_id = author_id;
        this.status = status;
    }

    public Blog(Long id, Long author_id, Integer status) {
        this.id = id;
        this.author_id = author_id;
        this.status = status;
    }

    public Blog(Long id, Long author_id, Integer status, String description) {
        this.id = id;
        this.author_id = author_id;
        this.status = status;
        this.description = description;
    }

    public Blog(Long id, Long author_id) {
        this.id = id;
        this.author_id = author_id;
    }

    public Blog(Long id) {
        this.id = id;
    }

    public Blog(){

    }

    private String description;
    private String content;

    public Blog(Long id, Long author_id, Integer status, String description, String content) {
        this.id = id;
        this.author_id = author_id;
        this.status = status;
        this.description = description;
        this.content = content;
    }
}
