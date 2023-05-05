package org.jdragon.springz.utils.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
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

    private String method = "GET";

    private String url;

    private final List<NameValuePair> nameValuePairs = new ArrayList<>();

    private final List<Header> headers = new ArrayList<>();

    private HttpEntity httpEntity;

    private final static Map<String, Class<? extends HttpRequestBase>> baseMethod = new HashMap<>();

    private final static Map<String, Class<? extends HttpEntityEnclosingRequestBase>> entityMethod = new HashMap<>();

    static {
        entityMethod.put("POST", HttpPost.class);
        entityMethod.put("PUT", HttpPut.class);
        entityMethod.put("PATCH", HttpPatch.class);
        baseMethod.put("GET", HttpGet.class);
        baseMethod.put("DELETE", HttpDelete.class);
        baseMethod.put("HEAD", HttpHead.class);
        baseMethod.put("OPTIONS", HttpOptions.class);
        baseMethod.put("TRACE", HttpTrace.class);
    }


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

    public static HttpUtils initJson() {
        return init().setHeader("Content-type", "application/json;charset=utf-8");
    }


    public HttpUtils setMethod(String method) {
        this.method = method;
        return this;
    }

    public HttpUtils setUrl(String url) {
        this.url = url;
        return this;
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
        if (body != null) {
            String bodyStr = JSON.toJSONString(body);
            if (!StrUtil.isBlank(bodyStr)) {
                this.httpEntity = new StringEntity(bodyStr, charset);
            }
        }
        return this;
    }

//    public HttpUtils setFile(String name, MultipartFile value) {
//        if (value != null) {
//            String fileName = value.getName();
//            try {
//                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//                builder.addBinaryBody(name, value.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);
//                this.httpEntity = builder.build();
//            } catch (IOException e) {
//                log.error(CommonConstants.DEFAULT_ERROR_MESSAGE, e);
//            }
//        }
//        return this;
//    }

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
        }
        return uri;
    }

    public Map<String, String> exec(String url) {
        this.url = url;
        try {
            return exec();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> exec() throws ReflectiveOperationException {
        if (StrUtil.isBlank(url)) {
            throw new HttpException("请求的url不允许为空");
        }
        URI uri = this.getUri(url);
        HttpRequestBase requestBase;
        if (entityMethod.containsKey(method)) {
            HttpEntityEnclosingRequestBase httpRequest = entityMethod.get(method)
                    .getConstructor(URI.class)
                    .newInstance(uri);
            if (httpEntity != null) {
                httpRequest.setEntity(httpEntity);
            }
            requestBase = httpRequest;
        } else if (baseMethod.containsKey(method)) {
            requestBase = baseMethod.get(method)
                    .getConstructor(URI.class)
                    .newInstance(uri);
        } else {
            throw new HttpException("不支持的请求方法" + method);
        }
        if (!CollectionUtils.isEmpty(headers)) {
            Header[] header = new Header[headers.size()];
            requestBase.setHeaders(headers.toArray(header));
        }
        return exec(requestBase);
    }

    public Map<String, String> exec(HttpRequestBase requestBase) {
        Map<String, String> resultMap = new HashMap<>();
        //执行post请求
        try {
            CloseableHttpResponse response = httpClient.execute(requestBase);
            return getHttpResult(response, requestBase, resultMap);
        } catch (Exception e) {
            requestBase.abort();
            resultMap.put("result", e.getMessage());
            log.error("获取http请求返回值失败 url={}", url);
            e.printStackTrace();
            throw new HttpException(e);
        }
    }

    /**
     * 获取请求返回值
     */
    private Map<String, String> getHttpResult(CloseableHttpResponse response, HttpUriRequest request, Map<String, String> resultMap) {

        String result = "";
        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                result = EntityUtils.toString(entity, charset);
                EntityUtils.consume(entity);//释放连接
            } catch (Exception e) {
                log.error("获取http请求返回值解析失败");
                request.abort();
            }
        }

        log.info("result :" + result);
        if (statusCode != 200) {
            log.warn("HttpClient status code :" + statusCode + "  request url===" + url);
            request.abort();
        }

        resultMap.put("statusCode", statusCode + "");
        resultMap.put("result", result);
        return resultMap;
    }
}
