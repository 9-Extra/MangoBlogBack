package cn.mango.mangoblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import jdk.jfr.Timestamp;
import lombok.Data;

@Data
@TableName(value = "blogs")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long authorid;
    private Integer status;
    //0 new&&!save
    //1 save
    //2 publish
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    private String description;
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    private String content;

    public Blog(Long id) {
        this.id = id;
    }
    public Blog(){

    }

    public Blog(Long id, Integer status) {
        this.id = id;
        this.status = status;
    }

    public Blog(Long id, Long author_id) {
        this.id = id;
        this.authorid = author_id;
    }

    public Blog(Long id, Long author_id, Integer status) {
        this.id = id;
        this.authorid = author_id;
        this.status = status;
    }

    public Blog(Long id, Long author_id, Integer status, String description) {
        this.id = id;
        this.authorid = author_id;
        this.status = status;
        this.description = description;
    }

    public Blog(Long id, Long author_id, Integer status, String description, String content) {
        this.id = id;
        this.authorid = author_id;
        this.status = status;
        this.description = description;
        this.content = content;
    }
}
