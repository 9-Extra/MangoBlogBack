package cn.mango.mangoblog.runtime;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.BlogOperation;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.mapper.BlogMapper;
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
        List<Blog> result = blogMapper.selectList(qw);
        if (result.isEmpty()) {
            return new ResultWrapper<>(400, "Not found", null);
        }
        return new ResultWrapper<>(result.get(0));
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
    public ResultWrapper<Long> insertBlog(Blog blog) {

        if (blogMapper.insert(blog) != 1) {//返回值1表示插入成功
            return new ResultWrapper<>(200, "Failure", null);
        } else return new ResultWrapper<>(0, "Success", blog.getId());//成功则返回blog_id

    }

    //更新blog并返回blog_id
    public boolean UpDateBlog(Blog blog) {
        UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
        blogUpdateWrapper.eq("id", blog.getId());
        return blogMapper.update(blog, blogUpdateWrapper) == 1;
    }



    public  boolean UpDateBlogStatus(Blog blog, Integer status,Long user_id) {
        blog.setStatus(status);
        UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
        blogUpdateWrapper.eq("id", blog.getId()).eq("authorid",user_id);
        return blogMapper.update(blog, blogUpdateWrapper) == 1;
    }

    public ResultWrapper<Long> UpdateBlogStatusByBlogOperation(BlogOperation operation,Integer status,Long user_id) {
        Blog blog = operation.getBlog();
        if (blog == null) {
            return new ResultWrapper<>(500, "Empty blog", 0L);
        }
        if (UpDateBlogStatus(blog, status,user_id)) {
            return new ResultWrapper<>(blog.getId());
        }
        else return new ResultWrapper<>(500,"Update blog failed,please check blog id",0L);
    }

    //删除blog
    public boolean deleteBlog(Long id,Long author_id) {
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("id",id).eq("authorid",author_id);
        return blogMapper.delete(qw) ==1;
    }


}
