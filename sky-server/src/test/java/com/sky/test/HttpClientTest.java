package com.sky.test;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author: fosss
 * Date: 2023/7/17
 * Time: 19:05
 * Description:
 */
@SpringBootTest
public class HttpClientTest {

    @Test
    public void testGet() throws IOException {
        //创建httpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建get请求
        HttpGet httpGet = new HttpGet("http://localhost:8080/admin/shop/status");
        //发送请求
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //获取响应码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("响应码：" + statusCode);
        //获取响应体
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println("响应体：" + body);

        //关闭资源
        response.close();
        httpClient.close();
    }

    @Test
    public void testPost() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");

        //构造json字符串
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "admin");
        jsonObject.put("password", "123456");
        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        stringEntity.setContentEncoding("utf-8");
        stringEntity.setContentType("application/json");

        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("响应码：" + statusCode);

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println("响应体：" + body);

        //关闭资源
        response.close();
        httpClient.close();
    }
}















