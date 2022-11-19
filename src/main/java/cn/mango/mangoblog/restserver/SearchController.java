package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.VerifyResult;
import cn.mango.mangoblog.utils.ElasticUtils;
import cn.mango.mangoblog.utils.TokenUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
public class SearchController {
    @GetMapping("/all/search")//用户根据keyword查询所有公开blog
    public void doSearch(HttpServletResponse response, @RequestParam(value = "keyword") String keyword) {
        String searchresult = ElasticUtils.SearchBlogsByKeywordAndStatus(response, keyword, 1,1);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            searchresult = "Fail to return search result due to IOException";
        }
        assert writer != null;
        writer.print(searchresult);
        writer.flush();
        writer.close();
    }

    @GetMapping("/private/search")//用户根据keyword查询所有公开blog
    public void doSearch(HttpServletResponse response, @RequestParam(value = "keyword") String keyword, @RequestHeader(value = "authorization") String token) {
        ResultWrapper<VerifyResult> verifyResultResultWrapper = TokenUtils.Verify(token);
        if (verifyResultResultWrapper.getData() != null) {
            Long user_id = verifyResultResultWrapper.getData().getId();
            String searchresult = ElasticUtils.SearchALLBlogsByKeyword(response, keyword, user_id);
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
                searchresult = "Fail to return search result due to IOException";
            }
            assert writer != null;
            writer.print(searchresult);
            writer.flush();
            writer.close();
        }
    }

    @GetMapping("/open/search")
    public void doSearch(HttpServletResponse response, @RequestParam(value = "keyword") String keyword, @RequestParam(value = "authorid") Long authorid) {
        String searchresult = ElasticUtils.SearchOpenBlogsByKeywordAndAuthorId(response, keyword, authorid);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            searchresult = "Fail to return search result due to IOException";
        }
        assert writer != null;
        writer.print(searchresult);
        writer.flush();
        writer.close();
    }
}

