package cn.mango.mangoblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "comments")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long blogid;
    private Long authorid;
    private String content;
//    private List<Long> replylist=new ArrayList<>();
//    private Long parentid;
}

