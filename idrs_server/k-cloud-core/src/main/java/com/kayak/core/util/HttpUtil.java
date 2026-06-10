package com.kayak.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;

public class HttpUtil {
    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常:" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String sendPost(String url, Map<String, Object> params) {
        CloseableHttpClient httpClient=null;
        CloseableHttpResponse response=null;
        try {
            String body = null;
            httpClient = HttpClients.createDefault();
            HttpPost postMethod = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000)
                    .setSocketTimeout(50000).setConnectTimeout(30000).build();
            postMethod.setConfig(requestConfig);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
            }
            HttpEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
            postMethod.setEntity(entity);
             response = httpClient.execute(postMethod);
            HttpEntity httpEntity = response.getEntity();
            if (entity != null) {
                body = EntityUtils.toString(httpEntity, "UTF-8");
            }
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
          try {
              if(null!=httpClient){
                  httpClient.close();
              }
              if(null!=response){
                  response.close();
              }
          }catch (Exception e){
              e.printStackTrace();
              return null;
          }
        }
    }

    public static String sendGet(String url, Map<String, Object> params) {
        CloseableHttpClient httpClient=null;
        CloseableHttpResponse response=null;
        try {
            String body = null;
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
                }
            }
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.setParameters(nvps);
            HttpGet getMethod = new HttpGet(uriBuilder.build());
            httpClient = HttpClients.createDefault();
             response = httpClient.execute(getMethod);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                body = EntityUtils.toString(httpEntity, "UTF-8");
            }
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(null!=httpClient){
                    httpClient.close();
                }
                if(null!=response){
                    response.close();
                }
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    /*入参说明
     *
     * param url 请求地址
     * param jsonObject 请求的json数据
     * param encoding 编码格式
     *
     * */
    public static String jsonPost(String url, String json) {
        String result = null;
        CloseableHttpClient httpClient=null;
        CloseableHttpResponse response=null;
        try {
            httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000)
                    .setSocketTimeout(50000).setConnectTimeout(30000).build();
            post.setConfig(requestConfig);
            StringEntity s = new StringEntity(json,"UTF-8");
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            response= httpClient.execute(post);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            try {
                if(null!=httpClient){
                    httpClient.close();
                }
                if(null!=response){
                    response.close();
                }
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 发送 http post 请求，支持文件上传
     */
    public static String sendPostFormMultipart(String url, Map<String,Object> params, MultipartFile file, Map<String,Object> headers, String encode) throws IOException {
        if(encode == null){
            encode = "utf-8";
        }
        url = handleUrlParam(url, params);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpPost httpost = new HttpPost(url);
        String result = null;
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpost.setHeader(entry.getKey(),entry.getValue().toString());
            }
        }
        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mEntityBuilder.setCharset(Charset.forName(encode));

        // 普通参数
        ContentType contentType = ContentType.create("text/plain",Charset.forName(encode));//解决中文乱码
        if (params != null && params.size() > 0) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                mEntityBuilder.addTextBody(key, params.get(key).toString(),contentType);
            }
        }
        mEntityBuilder.addBinaryBody("file", file.getInputStream(),ContentType.MULTIPART_FORM_DATA,file.getOriginalFilename());
        httpost.setEntity(mEntityBuilder.build());
        CloseableHttpResponse  httpResponse = null;
        try {
            HttpResponse res = closeableHttpClient.execute(httpost);
            result = EntityUtils.toString(res.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                httpResponse.close();
            } catch (Exception e) {

            }
        }
        try {  //关闭连接、释放资源
            closeableHttpClient.close();
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * 发送 http post 请求，支持文件上传
     */
    public static String sendPostFormMultipart(String url, Map<String,Object> params, MultipartFile[] files, Map<String,Object> headers, String encode) throws IOException {
        if(encode == null){
            encode = "utf-8";
        }
        url = handleUrlParam(url, params);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpPost httpost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000)
                .setSocketTimeout(50000).setConnectTimeout(30000).build();
        httpost.setConfig(requestConfig);
        String result = null;
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpost.setHeader(entry.getKey(),entry.getValue().toString());
            }
        }
        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mEntityBuilder.setCharset(Charset.forName(encode));

        // 普通参数
        ContentType contentType = ContentType.create("text/plain",Charset.forName(encode));//解决中文乱码
        if (params != null && params.size() > 0) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                mEntityBuilder.addTextBody(key, params.get(key).toString(),contentType);
            }
        }
        for(MultipartFile file:files){
            mEntityBuilder.addBinaryBody("files", file.getInputStream(),ContentType.MULTIPART_FORM_DATA,file.getOriginalFilename());
        }
        httpost.setEntity(mEntityBuilder.build());
        CloseableHttpResponse  httpResponse = null;
        try {
            HttpResponse res = closeableHttpClient.execute(httpost);
            result = EntityUtils.toString(res.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                httpResponse.close();
            } catch (Exception e) {

            }
        }
        try {  //关闭连接、释放资源
            closeableHttpClient.close();
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * url参数拼接
     *
     * @param url
     * @param urlParam
     * @return
     */
    private static String handleUrlParam(String url, Map<String, ?> urlParam) {
        if (urlParam == null || urlParam.isEmpty()) {
            return url;
        }
        Iterator<? extends Map.Entry<String, ?>> iterator = urlParam.entrySet().iterator();
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?");
        while (iterator.hasNext()) {
            Map.Entry<String, ?> entry = iterator.next();
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        return urlBuilder.toString();
    }
}


