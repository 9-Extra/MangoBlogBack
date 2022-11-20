package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.Collection;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.VerifyResult;
import cn.mango.mangoblog.runtime.BlogServiceImpl;
import cn.mango.mangoblog.runtime.CollectionServiceImpl;
import cn.mango.mangoblog.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class CollectionController {
    @Autowired
    private CollectionServiceImpl collectionService;
    @Autowired
    private BlogServiceImpl blogService;

    @GetMapping("/collection")//用户根据user_id获取所有收藏
    public ResultWrapper<List<Collection>> get_collections(@RequestHeader(value = "authorization")String token){
        ResultWrapper<VerifyResult> verifyResultResultWrapper= TokenUtils.Verify(token);
        if(verifyResultResultWrapper.getData()==null){
            return new ResultWrapper<>(verifyResultResultWrapper.getCode(), verifyResultResultWrapper.getMessage(), null);
        }
        Long user_id=verifyResultResultWrapper.getData().getId();
        List<Collection> collectionList=collectionService.get_collections_by_user_id(user_id);
         return new ResultWrapper<>(collectionList);
    }

    @PostMapping("/collection/add")
    public ResultWrapper<Boolean> add_collections(@RequestParam(value = "blogid")Long blog_id,@RequestHeader(value = "authorization")String token){
        ResultWrapper<VerifyResult> verifyResultResultWrapper= TokenUtils.Verify(token);
        if(verifyResultResultWrapper.getData()==null){
            return new ResultWrapper<>(verifyResultResultWrapper.getCode(), verifyResultResultWrapper.getMessage(), false);
        }
        Long user_id=verifyResultResultWrapper.getData().getId();
        Blog blog=blogService.GetOpenBlogById(blog_id);//检查blog是否公开
        if(blog!=null){
            Collection collection=new Collection(blog_id,user_id);
            if(collectionService.insert_collection(collection)){
                return new ResultWrapper<>(true);
            }
            return new ResultWrapper<>(500,"数据插入数据库失败",false);
        }
        return new ResultWrapper<>(400,"未找到指定公开blog",false);
    }

    @PostMapping("collection/delete")
    public ResultWrapper<Boolean> delete_collection(@RequestParam(value = "collectionid")Long collection_id,@RequestHeader(value = "authorization")String token){
        ResultWrapper<VerifyResult> verifyResultResultWrapper= TokenUtils.Verify(token);
        if(verifyResultResultWrapper.getData()==null){
            return new ResultWrapper<>(verifyResultResultWrapper.getCode(), verifyResultResultWrapper.getMessage(), false);
        }
        Long user_id=verifyResultResultWrapper.getData().getId();
        if(collectionService.delete_collection_by_collectoin_id_and_user_id(collection_id,user_id)){
            return new ResultWrapper<>(true);
        }
        return new ResultWrapper<>(400,"未找到指定收藏项",false);
    }

}
