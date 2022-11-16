package cn.mango.mangoblog.entity;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class BlogOperation {

    private Long blog_id;
    @Nullable
    private Blog blog;

    private String operation;

    private BlogOperation(Long blog_id, @Nullable Blog blog, String operation) {
        this.blog_id = blog_id;
        this.blog = blog;
        this.operation = operation;
    }

    public static final String OPERATION_NEW = "new";
    public static final String OPERATION_MODIFY = "edit";
    public static final String OPERATION_DELETE = "delete";
}
