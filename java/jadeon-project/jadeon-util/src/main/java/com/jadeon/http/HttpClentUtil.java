package com.jadeon.http;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

    static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * get请求
     *
     * @param url 第三方接口URL
     * @return
     * @throws Exception
     */
    public static StringBuilder get(String url) throws IOException {
        StringBuilder stringbuffer = null;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            get.setHeader("Accept", "application/json;charset=utf-8");
            get.setHeader(new BasicHeader("Content-type", "application/json;charset=utf-8"));
            // 参数设置
            RequestConfig config = RequestConfig.custom().setConnectTimeout(12500).setConnectionRequestTimeout(12000)
                    .setSocketTimeout(12500).build();
            get.setConfig(config);
            get.addHeader("Accept-Encoding", "gzip");

            httpResponse = httpClient.execute(get);
            stringbuffer = getResult(httpResponse.getEntity());

        } catch (Exception e) {
            logger.info("" + e);
        } finally {
            if (null != httpClient)
                httpClient.close();
            if (null != httpResponse)
                httpResponse.close();
        }
        return stringbuffer;
    }

    /**
     * post 请求
     *
     * @param url 第三方接口URL
     * @param data 参数
     * @return
     * @throws IOException
     */
    public static StringBuilder post(String url, String entity) throws IOException {
        StringBuilder stringbuffer = null;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.setHeader("Accept", "application/json;charset=utf-8");
            post.setHeader(new BasicHeader("Content-type", "application/json;charset=utf-8"));
            // 参数设置
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(7500)
                    .setConnectionRequestTimeout(7000).setSocketTimeout(8500).build();
            post.setConfig(requestConfig);
            post.setEntity((new StringEntity(data, "UTF-8")));

            httpResponse = httpClient.execute(post);
            stringbuffer = getResult(httpResponse.getEntity());
        } catch (Exception e) {
            logger.info("" + e);
        } finally {
            if (null != httpClient)
                httpClient.close();
            if (null != httpResponse)
                httpResponse.close();
        }
        return stringbuffer;
    }

    private static StringBuilder getResult(HttpEntity entity) throws IOException {
        StringBuilder stringBuffer = null;
        if (null != entity) {
            BufferedReader brReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            stringBuffer = new StringBuilder();
            String line = "";
            while ((line = brReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        }
        return stringBuffer;
    }

    /**
     * 图片下载
     * @param url 路径(http://xxx)
     * @param src 文件名(D:\xx.jpg)
     */
    private static void img(String url, String src){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                    .setSocketTimeout(50000).build();
            get.setConfig(requestConfig);
            get.addHeader("Content-Encoding", "gzip");

            response = httpClient.execute(get);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){
                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();
                src = "D:"+ File.separator + "img"+ File.separator + src;
                FileUtils.copyInputStreamToFile(in, new File(src));
                System.out.println("down img success!["+ url+"]");
            }
        } catch (Exception e) {
            logger.info("", e);
        } finally {
            try{
                if (null != httpClient)
                    httpClient.close();
                if (null != response)
                    response.close();
            }
            catch(Exception e){
                logger.info("", e.getMessage());
            }

        }
    }

}
