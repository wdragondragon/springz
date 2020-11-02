package org.jdragon.springz.utils.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.StrUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.27 22:34
 * @Description:
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private static final PoolingHttpClientConnectionManager pcm;//httpclient连接池

    private CloseableHttpClient httpClient = null; //http连接

    private final int connectionRequestTimeout = 3000;//从连接池获取连接超时时间

    private int connectTimeout = 3000;//连接超时时间

    private int socketTimeout = 3000;//获取数据超时时间

    private final String charset = "UTF-8";

    private RequestConfig requestConfig = null;//请求配置

    private RequestConfig.Builder requestConfigBuilder = null;//build requestConfig

    private final List<NameValuePair> nameValuePairs = new ArrayList<>();

    private final List<Header> headers = new ArrayList<>();

    private String body = "";

    static {
        pcm = new PoolingHttpClientConnectionManager();
        pcm.setMaxTotal(50);//整个连接池最大连接数
        pcm.setDefaultMaxPerRoute(50);//每路由最大连接数，默认值是2
    }

    private static HttpUtils defaultInit() {
        HttpUtils httpUtils = new HttpUtils();
        if (httpUtils.requestConfig == null) {
            httpUtils.requestConfigBuilder = RequestConfig.custom().setConnectTimeout(httpUtils.connectTimeout)
                    .setConnectionRequestTimeout(httpUtils.connectionRequestTimeout)
                    .setSocketTimeout(httpUtils.socketTimeout);
            httpUtils.requestConfig = httpUtils.requestConfigBuilder.build();
        }
        return httpUtils;
    }

    /**
     * 初始化 httpUtil
     */
    public static HttpUtils init() {
        HttpUtils httpUtils = defaultInit();
        if (httpUtils.httpClient == null) {
            httpUtils.httpClient = HttpClients.custom().setConnectionManager(pcm).build();
        }
        return httpUtils;
    }

    public static HttpUtils initJson(){
        return init().setHeader("Content-type","application/json;charset=utf-8");
    }

    /**
     * 设置请求头
     */
    public HttpUtils setHeader(String name, String value) {
        Header header = new BasicHeader(name, value);
        headers.add(header);
        return this;
    }

    /**
     * 设置请求头
     */
    public HttpUtils setHeaderMap(Map<String, String> headerMap) {
        for (Map.Entry<String, String> param : headerMap.entrySet()) {
            Header header = new BasicHeader(param.getKey(), param.getValue());
            headers.add(header);
        }
        return this;
    }

    /**
     * 设置请求参数
     */
    public HttpUtils setParam(String name, String value) {
        nameValuePairs.add(new BasicNameValuePair(name, value));
        return this;
    }

    /**
     * 设置请求参数
     */
    public HttpUtils setParamMap(Map<String, String> paramMap) {
        for (Map.Entry<String, String> param : paramMap.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }
        return this;
    }

    /**
     * 设置字符串参数
     */
    public HttpUtils setBody(Object body) {
        this.body = JSON.toJSONString(body);
        return this;
    }

    /**
     * 设置连接超时时间
     */
    public HttpUtils setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        this.requestConfigBuilder = requestConfigBuilder.setConnectTimeout(connectTimeout);
        requestConfig = requestConfigBuilder.build();
        return this;
    }
    /**
     * 设置连接超时时间
     */
    public HttpUtils setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        this.requestConfigBuilder = requestConfigBuilder.setSocketTimeout(socketTimeout);
        requestConfig = requestConfigBuilder.build();
        return this;
    }

    private URI getUri(String url) {
        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (!CollectionUtils.isEmpty(nameValuePairs)) {
                uriBuilder.setParameters(nameValuePairs);
            }
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            log.error("url 地址异常");
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * http get 请求
     */
    public Map<String, String> get(String url) {
        Map<String, String> resultMap = new HashMap<>();
        //获取请求URI
        URI uri = getUri(url);
        log.info("get url :" + url);
        log.info("params :" + nameValuePairs);
        if (uri != null) {
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig);
            if (!CollectionUtils.isEmpty(headers)) {
                Header[] header = new Header[headers.size()];
                httpGet.setHeaders(headers.toArray(header));
            }

            //执行get请求
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                return getHttpResult(response, url, httpGet, resultMap);
            } catch (Exception e) {
                httpGet.abort();
                resultMap.put("result", e.getMessage());
                log.error("获取http GET请求返回值失败 url======" + url);
                e.printStackTrace();
                throw new HttpException(e);
            }
        }
        return resultMap;
    }

    /**
     * http post 请求
     */
    public Map<String, String> post(String url){
        URI uri = this.getUri(url);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setConfig(requestConfig);
        if (!CollectionUtils.isEmpty(headers)) {
            Header[] header = new Header[headers.size()];
            httpPost.setHeaders(headers.toArray(header));
        }
//        if (!CollectionUtils.isEmpty(nameValuePairs)) {
//            try {
//                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
//            } catch (Exception e) {
//                log.error("http post entity form error", e);
//                throw new HttpException(e);
//            }
//        }
        if (!StrUtil.isEmpty(body) && !body.equals("")) {
            try {
                httpPost.setEntity(new StringEntity(body, charset));
            } catch (UnsupportedCharsetException e) {
                log.error("http post entity form error");
                e.printStackTrace();
                throw new HttpException(e);
            }
        }

        log.info("post url :" + url);
        log.info("params :" + nameValuePairs);

        Map<String, String> resultMap = new HashMap<>();
        //执行post请求
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            return getHttpResult(response, url, httpPost, resultMap);
        } catch (Exception e) {
            httpPost.abort();
            resultMap.put("result", e.getMessage());
            log.error("获取http POST请求返回值失败 url======" + url);
            e.printStackTrace();
            throw new HttpException(e);
        }
        //return resultMap;
    }

    /**
     * 获取请求返回值
     */
    private Map<String, String> getHttpResult(CloseableHttpResponse response, String url, HttpUriRequest request, Map<String, String> resultMap) {

        String result = "";
        int statusCode = response.getStatusLine().getStatusCode();
        resultMap.put("statusCode", statusCode + "");
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                result = EntityUtils.toString(entity, charset);
                EntityUtils.consume(entity);//释放连接
            } catch (Exception e) {
                log.error("获取http请求返回值解析失败");
                e.printStackTrace();
                request.abort();
            }
        }
        if (statusCode != 200) {
            result = "HttpClient status code :" + statusCode + "  request url===" + url;
            log.info("HttpClient status code :" + statusCode + "  request url===" + url);
            log.info("Message :" + result);
            request.abort();
        }
        log.info("result :" + result);
        resultMap.put("result", result);
        return resultMap;
    }
}
