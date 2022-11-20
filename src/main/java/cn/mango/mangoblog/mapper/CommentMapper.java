package cn.mango.mangoblog.mapper;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
}
