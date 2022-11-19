package cn.mango.mangoblog.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ElasticUtils {
    public static String SearchBlogsByKeywordAndStatus(HttpServletResponse response, String keyword,Integer statusauthor,Integer statusadmin) {//根据keyword和status搜索blog
        try {
            if (StringUtils.isBlank(keyword)) {//如果是空查询，则返回
                return "Empty search";
            }
            response.setHeader("Content-Type", "text/html; charset=utf-8");//设置传输编码格式
            //初始化RestClient
            RestClient restClient = RestClient.builder(
                    new HttpHost("127.0.0.1", 9200, "http")).build();
            //设置请求方式和地址
            Request request = new Request(
                    "GET",
                    "blogs/_search");
            String search = "{\"query\": {\"function_score\": {\"query\": {\"bool\": {\"should\": [ {\"match\": {\"content\": \"" + keyword + "\"}}, { \"match\": {\"description\": \"" + keyword + "\"}}],\"must\":[ {\"match\": {\"statusauthor\": \""+statusauthor+"\"}},{\"match\": {\"statusadmin\":\""+statusadmin+"\"}}]}},\"min_score\": 2}},\"from\": 0,\"size\": 2,\"sort\": [ {\"_score\": {\"order\": \"desc\"}}]}";
            //基于json构造查询
            request.setJsonEntity(search);
            Response esRes = restClient.performRequest(request);//esRs:查询的返回值
//        log.info("{}", esRes);
//        RequestLine requestLine = esRes.getRequestLine();
//        HttpHost host = esRes.getHost();
//        int statusCode = esRes.getStatusLine().getStatusCode();
//        Header[] headers = esRes.getHeaders();
            String responseBody = EntityUtils.toString(esRes.getEntity());
//        log.info("{}{}{}{}{}", requestLine, host, statusCode, headers, responseBody);
            System.out.println(responseBody);//打印搜索结果到控制台
            restClient.close();
            return responseBody;
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Search failed due to RuntimeException";
        }
    }
    public static String SearchALLBlogsByKeyword(HttpServletResponse response, String keyword,Long authorid) {//用户根据关键词在自己的所有blog中搜索
        try {
            if (StringUtils.isBlank(keyword)) {//如果是空查询，则返回
                return "Empty search";
            }
            response.setHeader("Content-Type", "text/html; charset=utf-8");//设置传输编码格式
            //初始化RestClient
            RestClient restClient = RestClient.builder(
                    new HttpHost("127.0.0.1", 9200, "http")).build();
            //设置请求方式和地址
            Request request = new Request(
                    "GET",
                    "blogs/_search");
            String search = "{\"query\": {\"function_score\": {\"query\": {\"bool\": {\"should\": [ {\"match\": {\"content\": \"" + keyword + "\"}}, { \"match\": {\"description\": \"" + keyword + "\"}}],\"must\":[ {\"match\": {\"aguthorid\": \""+authorid+"\"}}]}},\"min_score\": 2}},\"from\": 0,\"size\": 2,\"sort\": [ {\"_score\": {\"order\": \"desc\"}}]}";
            //基于json构造查询
            request.setJsonEntity(search);
            Response esRes = restClient.performRequest(request);//esRs:查询的返回值
//        log.info("{}", esRes);
//        RequestLine requestLine = esRes.getRequestLine();
//        HttpHost host = esRes.getHost();
//        int statusCode = esRes.getStatusLine().getStatusCode();
//        Header[] headers = esRes.getHeaders();
            String responseBody = EntityUtils.toString(esRes.getEntity());
//        log.info("{}{}{}{}{}", requestLine, host, statusCode, headers, responseBody);
            System.out.println(responseBody);//打印搜索结果到控制台
            restClient.close();
            return responseBody;
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Search failed due to RuntimeException";
        }
    }
    public static String SearchOpenBlogsByKeywordAndAuthorId(HttpServletResponse response, String keyword,Long authorid) {//读者根据关键词在author的所有open blog中搜索
        try {
            if (StringUtils.isBlank(keyword)) {//如果是空查询，则返回
                return "Empty search";
            }
            response.setHeader("Content-Type", "text/html; charset=utf-8");//设置传输编码格式
            //初始化RestClient
            RestClient restClient = RestClient.builder(
                    new HttpHost("127.0.0.1", 9200, "http")).build();
            //设置请求方式和地址
            Request request = new Request(
                    "GET",
                    "blogs/_search");
            String search = "{\"query\": {\"function_score\": {\"query\": {\"bool\": {\"should\": [ {\"match\": {\"content\": \"" + keyword + "\"}}, { \"match\": {\"description\": \"" + keyword + "\"}}],\"must\":[ {\"match\": {\"authorid\": \""+authorid+"\"}},{\"match\": {\"statusauthor\":\"1\"}},{\"match\":{\"statusadmin\":\"1\"}}]}},\"min_score\": 2}},\"from\": 0,\"size\": 2,\"sort\": [ {\"_score\": {\"order\": \"desc\"}}]}";
            //基于json构造查询
            request.setJsonEntity(search);
            Response esRes = restClient.performRequest(request);//esRs:查询的返回值
//        log.info("{}", esRes);
//        RequestLine requestLine = esRes.getRequestLine();
//        HttpHost host = esRes.getHost();
//        int statusCode = esRes.getStatusLine().getStatusCode();
//        Header[] headers = esRes.getHeaders();
            String responseBody = EntityUtils.toString(esRes.getEntity());
//        log.info("{}{}{}{}{}", requestLine, host, statusCode, headers, responseBody);
            System.out.println(responseBody);//打印搜索结果到控制台
            restClient.close();
            return responseBody;
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Search failed due to RuntimeException";
        }
    }

}
