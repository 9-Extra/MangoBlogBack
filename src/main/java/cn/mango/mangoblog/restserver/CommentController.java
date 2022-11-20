package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.Comment;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.VerifyResult;
import cn.mango.mangoblog.runtime.BlogServiceImpl;
import cn.mango.mangoblog.runtime.CommentServiceImpl;
import cn.mango.mangoblog.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class CommentController {
    @Autowired
    CommentServiceImpl commentService;
    @Autowired
    BlogServiceImpl blogService;
    @GetMapping("/comment/get/{id}")//用户阅读blog时获取blog的评论
    public ResultWrapper<List<Comment>> GetComments(@PathVariable(value = "id")Long blogid){
        List<Comment> commentList =commentService.GetComments(blogid);
         return new ResultWrapper<>(commentList);
    }

    @PostMapping("/comment/add")//用户对公开的blog添加评论
    public ResultWrapper<Boolean> AddComment(@RequestBody Comment comment,@RequestHeader(value = "authorization")String token){
        if(blogService.GetOpenBlogById(comment.getBlogid())==null)//先判断是否存在对应的公开Blog
            return new ResultWrapper<>(400,"未找到指定的公开blog",false);
        if(comment.getContent()==null)//判断是否评论的content为空
            return new ResultWrapper<>(3,"评论内容不能为空",false);
        ResultWrapper<VerifyResult> verifyResultResultWrapper= TokenUtils.Verify(token);
        if(verifyResultResultWrapper.getData()==null){
            return new ResultWrapper<>(verifyResultResultWrapper.getCode(), verifyResultResultWrapper.getMessage(), false);
        }
        else comment.setAuthorid(verifyResultResultWrapper.getData().getId());
        if (commentService.insertComment(comment)) {
            return new ResultWrapper<>(true);
        }
        else return new ResultWrapper<>(500, "新建评论失败", false);

    }

    @PostMapping("/comment/delete")//写评论的人或评论作者删除评论
    public ResultWrapper<Boolean> DeleteComment(@RequestParam(value = "commentid")Long commentid,@RequestHeader(value = "authorization")String token) {
        ResultWrapper<VerifyResult> verifyResultResultWrapper = TokenUtils.Verify(token);
        if (verifyResultResultWrapper.getData() == null) {
            return new ResultWrapper<>(verifyResultResultWrapper.getCode(), verifyResultResultWrapper.getMessage(), false);
        }
        Integer privilege = verifyResultResultWrapper.getData().getPrivilege();
        Long user_id = verifyResultResultWrapper.getData().getId();
        List<Comment> commentList = commentService.GetCommentByCommentId(commentid);
        if (commentList.isEmpty()) {
            return new ResultWrapper<>(400, "未找到指定评论", false);
        }
        Comment comment = commentList.get(0);
        Blog blog = blogService.GetBlogById(comment.getBlogid());
        if (privilege != 0 || Objects.equals(user_id, comment.getAuthorid()) || Objects.equals(user_id, blog.getAuthorid())) {//如果用户为管理员/发布评论的人/blog作者
            if (commentService.DeleteCommentByCommtId(commentid)) {
                return new ResultWrapper<>(true);
            }
            return new ResultWrapper<>(500, "数据库数据删除失败", false);
        }
        return new ResultWrapper<>(2, "权限不足", false);
    }
}
