package cn.mango.mangoblog.runtime;

import cn.mango.mangoblog.entity.Blog;
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
    public Blog GetBlogById(Long id) {
        //查询构造器
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("id", id).ne("statusauthor",-1);
        List<Blog> result = blogMapper.selectList(qw);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
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

    public Blog GetOpenBlogById(Long id) {
        //查询构造器
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("id", id).eq("statusauthor",1).eq("statusadmin",1);
        List<Blog> result = blogMapper.selectList(qw);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
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


    public List<Blog> GetBlogsByStauts( Integer statusauthor,Integer statusadmin) {//管理员获取审核不通过/未审核的blog用
    QueryWrapper<Blog> qw = new QueryWrapper<>();
    qw.eq("statusauthor", statusauthor).eq("statusadmin",statusadmin);
    return blogMapper.selectList(qw);
    }

    public List<Blog> GetBlogsByStautsAdmin( Integer statusadmin) {//管理员获取审核通过的blog用
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("statusadmin",statusadmin);
        return blogMapper.selectList(qw);
    }

    public List<Blog> GetBlogsByAuthorIdAndStauts(Long author_id, Integer statusauthor,Integer statusadmin) {
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("authorid", author_id).eq("statusauthor", statusauthor).eq("statusadmin",statusadmin);
        return blogMapper.selectList(qw);
    }

    //插入blog并返回blog_id
    public Long insertBlog(Blog blog) {
        if (blogMapper.insert(blog) == 1) {//返回值1表示插入成功
            return blog.getId();
        } else {
            return null;
        }

    }

    //更新blog并返回blog_id
    public boolean UpDateBlog(Long blog_id, Long author_id, String description, String content) {
        UpdateWrapper<Blog> uw = new UpdateWrapper<>();
        uw.eq("id", blog_id)
                .eq("authorid", author_id)
                .eq("statusauthor", -1)
                .set("statusauthor", 0);//如果是白板，将其设置为非公开
        blogMapper.update(null, uw);
        uw.clear();

        uw.eq("id", blog_id)
                .eq("authorid", author_id)
                .set("description", description)
                .set("content", content)
                .set("statusadmin",-1);//设置blog状态为未审核
        return blogMapper.update(null, uw) == 1;
    }

    //作者用，修改博客状态（检查作者）
    public boolean update_blog_state_author(long blog_id, long author_id, Integer status_author, Integer status_admin){
        UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
        blogUpdateWrapper
                .eq("id", blog_id)
                .eq("authorid", author_id)
                .set(status_author != null && status_author != 0, "statusauthor", status_author)
                .set(status_admin != null, "statusadmin", status_admin);

        return blogMapper.update(null, blogUpdateWrapper) == 1;
    }

    //管理员用，修改博客状态（不检查作者）
    public boolean update_blog_state_admin(long blog_id, Integer status_author, Integer status_admin){
        UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
        blogUpdateWrapper
                .eq("id", blog_id)
                .set(status_author != null && status_author != 0, "statusauthor", status_author)
                .set(status_admin != null, "statusadmin", status_admin);

        return blogMapper.update(null, blogUpdateWrapper) == 1;
    }
    public void update_blog_count(Long blog_id) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", blog_id);
        wrapper.setSql("count = count + 1");
        blogMapper.update(null,wrapper);
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

//    public Boolean UpdateBlogAndStatusUser(Blog blog,Integer status){
//        if(status==0){
//            return false;
//        }
//        blog.setStatus(status);
//        blogMapper.updateById(blog);
//    }

    //删除blog
    public boolean deleteBlog(Long id,Long author_id) {
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("id",id).eq("authorid",author_id);
        return blogMapper.delete(qw) ==1;
    }


}
