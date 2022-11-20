package cn.mango.mangoblog.runtime;

import cn.mango.mangoblog.entity.Collection;
import cn.mango.mangoblog.entity.Comment;
import cn.mango.mangoblog.mapper.CollectionMapper;
import cn.mango.mangoblog.restserver.CollectionController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class CollectionServiceImpl {
    @Autowired
    private CollectionMapper collectionMapper;

    public Boolean insert_collection(Collection collection){//新建收藏
        if (collectionMapper.insert(collection) == 1) {//返回值1表示插入成功
            return true;
        } else {
            return null;
        }
    }

    public List<Collection> get_collections_by_user_id(Long user_id){//根据userid获取所有收藏
        QueryWrapper<Collection> qw=new QueryWrapper<>();
        qw.eq("authorid",user_id);
        return collectionMapper.selectList(qw);
    }

    public Boolean delete_collection_by_collectoin_id_and_user_id(Long collection_id,Long user_id){
        QueryWrapper<Collection> qw=new QueryWrapper<>();
        qw.eq("id",collection_id).eq("userid",user_id);
        return collectionMapper.delete(qw)==1;
    }

//    public Boolean dlete_collections_by_blog_id(Long blogid){
//        QueryWrapper<Collection> qw = new QueryWrapper<>();
//        qw.eq("id",blogid);
//        return commentMapper.delete(qw) ==1;
//    }

}
