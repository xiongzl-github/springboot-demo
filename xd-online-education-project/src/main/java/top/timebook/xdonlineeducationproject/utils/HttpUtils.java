package top.timebook.xdonlineeducationproject.utils;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HttpUtils
 * @Description 封装http get post 方法
 * @Author xiongzl
 * @Date 2019/5/6 14:32
 * @Version 1.0
 **/
public class HttpUtils {

    private static final Gson gson = new Gson();

    private static final Integer SUCCESS_CODE = 200;

    private static final Integer TIME_OUT = 5000;

    /**
     * @return java.util.Map
     * @Author xiongzl
     * @Description 模拟get请求
     * @Date 2019/5/6 18:08
     * @Param [url]
     **/
    public static Map<String, Object> doGet(String url) {
        Map<String,Object> map = new HashMap<String, Object>(100);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 设置连接超时, 请求超时, 时候可以重定向
        RequestConfig requestConfig = setRequest();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpUtils.SUCCESS_CODE) {
                String jsonResult = EntityUtils.toString(httpResponse.getEntity());
                System.out.println("jsonResult: ");
                System.out.println(jsonResult);
                map = gson.fromJson(jsonResult, map.getClass());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeHttpClient(httpClient);
        }
        return map;
    }

    /**
     * @return java.lang.String
     * @Author xiongzl
     * @Description 模拟post请求
     * @Date 2019/5/6 18:08
     * @Param [url, data, timeout] 请求url, 请求参数, timeout时长
     **/
    public static String doPost(String url, String data, int timeout) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 设置连接超时, 请求超时, 时候可以重定向
        RequestConfig requestConfig = setRequest(timeout);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "text/html; chartset=UTF-8");
        httpPost.setConfig(requestConfig);
        if (data != null) {
            StringEntity stringEntity = new StringEntity(data, "UTF-8");
            httpPost.setEntity(stringEntity);
        }
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() == HttpUtils.SUCCESS_CODE) {
                result = EntityUtils.toString(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeHttpClient(httpClient);
        }
        return result;
    }


    /**
     * @param timeout
     * @return org.apache.http.client.config.RequestConfig
     * @Author xiongzl
     * @Description 设置Request请求
     * @Date 2019/5/6 15:50
     **/
    private static RequestConfig setRequest(int timeout) {
        return RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .setRedirectsEnabled(true)
                .build();
    }

    /**
     * @return org.apache.http.client.config.RequestConfig
     * @Author xiongzl
     * @Description 设置Request请求
     * @Date 2019/5/6 15:50
     * @Param []
     **/
    private static RequestConfig setRequest() {
        return RequestConfig.custom().setConnectTimeout(TIME_OUT)
                .setConnectionRequestTimeout(TIME_OUT)
                .setSocketTimeout(TIME_OUT)
                .setRedirectsEnabled(true)
                .build();

    }

    /**
     * @return void
     * @Author xiongzl
     * @Description 关闭HttpClient请求
     * @Date 2019/5/6 15:51
     * @Param [httpClient]
     **/
    private static void closeHttpClient(CloseableHttpClient httpClient) {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
