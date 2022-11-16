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
    public ResultWrapper<Blog> getBlog(Long id){
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
    public boolean insertBlog(Blog blog){
        return blogMapper.insert(blog) == 1;//返回值1表示插入成功
    }

    //更新blog并返回blog_id
    public boolean updateBlog(Blog blog){
        UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
        blogUpdateWrapper.eq("id", blog.getId()).eq("author_id", blog.getAuthor_id());
        return blogMapper.update(blog, blogUpdateWrapper) == 1;
    }

    //删除blog
    public boolean deleteBlog(Long id){
        return blogMapper.deleteById(id) == 1;
    }
}
