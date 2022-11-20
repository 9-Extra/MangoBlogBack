package cn.mango.mangoblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "collections")
public class Collection {
    @TableId(type = IdType.AUTO)
    private Long id;//该条收藏的id
    private Long blogid;//被收藏的blog
    private Long userid;//收藏者

    public Collection(Long blogid, Long userid) {
        this.blogid = blogid;
        this.userid = userid;
    }
}
