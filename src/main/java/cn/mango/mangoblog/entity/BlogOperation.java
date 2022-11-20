package cn.mango.mangoblog.entity;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class BlogOperation {

    private Long blog_id;
    @Nullable
    private String description;
    @Nullable
    private String content;

    private String operation;

    public BlogOperation(Long blog_id, @Nullable Integer status, @Nullable String description, @Nullable String content, String operation) {
        this.blog_id = blog_id;
//        this.status = status;
        this.description = description;
        this.content = content;
        this.operation = operation;
    }

    public static final String OPERATION_NEW = "new";//新建空白Blog
    public static final String OPERATION_SAVE = "save";//保存到草稿箱
    public static final String OPERATION_POST="post";//发布blog
    public static final String OPERATION_MODIFY = "edit";//只修改博客内容，保持状态不变
    public static final String OPERATION_DELETE = "delete";//删除blog，仅作者
    public static final String OPERATION_REVOKE ="revoke";//blog的下架，仅管理员

    public static final String OPERATION_AGREE ="agree";//同意发布
}
