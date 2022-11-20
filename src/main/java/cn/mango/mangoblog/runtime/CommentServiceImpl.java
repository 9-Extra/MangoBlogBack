package cn.mango.mangoblog.runtime;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.Comment;
import cn.mango.mangoblog.mapper.BlogMapper;
import cn.mango.mangoblog.mapper.CommentMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
@Primary
@Service
public class CommentServiceImpl {
    @Autowired
    private CommentMapper commentMapper;

    //插入新comment
    public  Boolean insertComment(Comment comment) {
        if (commentMapper.insert(comment) == 1) {//返回值1表示插入成功
            return true;
        } else {
            return null;
        }
    }
    //删除comment
    public Boolean DeleteComment(Long id){
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("id",id);
        return commentMapper.delete(qw) ==1;
    }
    //根据blogid获取comments
    public List<Comment> GetComments(Long blogid){
        QueryWrapper<Comment> qw=new QueryWrapper<>();
        qw.eq("blogid",blogid);
        return commentMapper.selectList(qw);
    }
    public List<Comment> GetCommentByCommentId(Long commentid){//根据commentid获取评论
        QueryWrapper<Comment> qw=new QueryWrapper<>();
        qw.eq("commentid",commentid);
        return commentMapper.selectList(qw);
    }

}
