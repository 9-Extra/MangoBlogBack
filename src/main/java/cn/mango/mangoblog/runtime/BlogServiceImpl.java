package cn.mango.mangoblog.runtime;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.BlogOperation;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.mapper.BlogMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class BlogServiceImpl {
    @Autowired
    private  BlogMapper blogMapper;

    //获取Blog
    public ResultWrapper<Blog> GetBlogById(Long id) {
        //查询构造器
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("id", id);
        List<Blog> result = blogMapper.selectList(qw);
        if (result.isEmpty()) {
            return new ResultWrapper<>(400, "Not found", null);
        }
        return new ResultWrapper<>(result.get(0));
    }
    public ResultWrapper<Blog> GetBlogByIdAndAuthorId(Long id,  Long author_id) {
        //查询构造器
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("id", id).eq("authorid",author_id);
        Blog result = blogMapper.selectOne(qw);
        if (result == null) {
            return new ResultWrapper<>(400, "Not found", null);
        }
        return new ResultWrapper<>(result);
    }

    public ResultWrapper<Blog> GetOpenBlogById(Long id) {
        //查询构造器
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("id", id).eq("status",2);
        List<Blog> result = blogMapper.selectList(qw);
        if (result.isEmpty()) {
            return new ResultWrapper<>(400, "Not found", null);
        }
        return new ResultWrapper<>(result.get(0));
    }

    public ResultWrapper<List<Blog>> GetAllBlogsByAuthorId(Long author_id){
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("authorid",author_id);
        List<Blog> result = blogMapper.selectList(qw);
        if (result.isEmpty()) {
            return new ResultWrapper<>(400, "Not found", null);
        }
        return new ResultWrapper<>(result);
    }

//    public ResultWrapper<List<Blog>> GetOpenBlogsByAuthorId(Long author_id){
//        QueryWrapper<Blog> qw = new QueryWrapper<>();
//        qw.eq("authorid",author_id).eq("status",2);
//        List<Blog> result = blogMapper.selectList(qw);
//        if (result.isEmpty()) {
//            return new ResultWrapper<>(400, "Not found", null);
//        }
//        return new ResultWrapper<>(result);
//    }


    public ResultWrapper<List<Blog>> GetBlogsByAuthorIdAndStauts(Long author_id, Integer status) {
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("authorid", author_id).eq("status", status);
        List<Blog> result = blogMapper.selectList(qw);
        if (result.isEmpty()) {
            return new ResultWrapper<>(400, "Not found", null);
        }
        return new ResultWrapper<>(0, "Success", result);
    }

    //插入blog并返回blog_id
    public Long insertBlog(Blog blog) {
        if (blogMapper.insert(blog) != 1) {//返回值1表示插入成功
            return blog.getId();
        } else {
            return null;
        }

    }

    //更新blog并返回blog_id
    public boolean UpDateBlog(Long blog_id, Long author_id, String description, String content) {
        UpdateWrapper<Blog> uw = new UpdateWrapper<>();
        uw.eq("id", blog_id).eq("authorid", author_id).set("description", description).set("content", content);
        return blogMapper.update(null, uw) == 1;
    }


    //以管理员权限，不修改博客内容，仅修改状态
    public boolean upDateBlogStatusAdmin(Long blog_id, Integer status) {
        UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
        blogUpdateWrapper.eq("id", blog_id).set("status", status);
        return blogMapper.update(null, blogUpdateWrapper) == 1;
    }


    //以用户权限，不修改博客内容，仅修改状态
    public boolean upDateBlogStatusUser(Long blog_id, Integer status, Long user_id) {
        UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
        blogUpdateWrapper.eq("id", blog_id).eq("authorid",user_id).set("status", status);
        return blogMapper.update(null, blogUpdateWrapper) == 1;
    }
    //仅查询blog的作者
    public Long getBlogAuthor(Long blog_id){
        LambdaQueryWrapper<Blog> qw = new LambdaQueryWrapper<>();
        qw.eq(Blog::getId, blog_id).select(Blog::getAuthorid);

        Blog blog = blogMapper.selectOne(qw);
        if (blog == null) {
            return null;
        } else {
            return blog.getAuthorid();
        }
    }

    //删除blog
    public boolean deleteBlog(Long id,Long author_id) {
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("id",id).eq("authorid",author_id);
        return blogMapper.delete(qw) ==1;
    }


}
