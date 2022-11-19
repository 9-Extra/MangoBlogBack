package cn.mango.mangoblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName(value = "blogs")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long authorid;
    private Integer statusauthor;//-1白板，0非公开，1公开
    private Integer statusadmin;//-1审核中，0审核通过，1失败
    //0 new&&!save
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    private String description;
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    private String content;
    public Blog(Long id, Long authorid, Integer statusauthor, Integer statusadmin, String description, String content) {
        this.id = id;
        this.authorid = authorid;
        this.statusauthor = statusauthor;
        this.statusadmin = statusadmin;
        this.description = description;
        this.content = content;
    }



    public Blog(Long id) {
        this.id = id;
    }
    public Blog(){

    }

    public Blog(Long id, Integer statusauthor) {
        this.id = id;
        this.statusauthor = statusauthor;
    }

    public Blog(Long id, Long author_id) {
        this.id = id;
        this.authorid = author_id;
    }

    public Blog(Long id, Long author_id, Integer statusauthor) {
        this.id = id;
        this.authorid = author_id;
        this.statusauthor = statusauthor;
    }

    public Blog(Long id, Long author_id, Integer statusauthor, String description) {
        this.id = id;
        this.authorid = author_id;
        this.statusauthor = statusauthor;
        this.description = description;
    }

    public Blog(Long id, Long author_id, Integer statusauthor, String description, String content) {
        this.id = id;
        this.authorid = author_id;
        this.statusauthor = statusauthor;
        this.description = description;
        this.content = content;
    }
}
