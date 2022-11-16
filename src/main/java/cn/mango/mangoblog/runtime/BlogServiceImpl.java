package cn.mango.mangoblog.runtime;

import cn.mango.mangoblog.entity.Blog;
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

    private BlogMapper blogMapper;
    //获取Blog
    public ResultWrapper<Blog> GetBlogById(Long id){
        //查询构造器
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("id", id);
        List<Blog> result = blogMapper.selectList(qw);
        if (result.isEmpty()){
            return new ResultWrapper<>(400, "Not found", null);
        }
        return new ResultWrapper<>(result.get(0));
    }

    //插入blog并返回blog_id
    public ResultWrapper<Long> insertBlog(Blog blog){

        if(blogMapper.insert(blog)!=1) {//返回值1表示插入成功
            return new ResultWrapper<>(200,"Failure",null);
        }
        else return new ResultWrapper<>(0,"Success",blog.getId());//成功则返回blog_id

    }

    //更新blog并返回blog_id
    public boolean UpDateBlog(Blog blog){
        UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
        blogUpdateWrapper.eq("id", blog.getId());
        return blogMapper.update(blog, blogUpdateWrapper) == 1;
    }

    public boolean UpDateBlogStatus(Blog blog,Integer status){
        blog.setStatus(status);
        UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
        blogUpdateWrapper.eq("id", blog.getId());
        return blogMapper.update(blog, blogUpdateWrapper) == 1;
    }

    //删除blog
    public boolean deleteBlog(Long id){
        return blogMapper.deleteById(id) == 1;
    }

    public ResultWrapper<List<Blog>> GetBlogByAuthorIdAndStauts(Long author_id,Integer status){
        QueryWrapper<Blog> qw=new QueryWrapper<>();
        qw.eq("author_id",author_id).eq("status",status);
        List<Blog> result=blogMapper.selectList(qw);
        if(result.isEmpty()){
            return new ResultWrapper<>(400,"Not found",null);
        }
        return new ResultWrapper<>(0,"Success",result);
    }
}
