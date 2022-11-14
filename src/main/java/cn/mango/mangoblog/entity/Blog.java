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

    private String description;
    private String content;

    public Blog(Long id, Long author_id, String description, String content) {
        this.id = id;
        this.author_id = author_id;
        this.description = description;
        this.content = content;
    }
}
