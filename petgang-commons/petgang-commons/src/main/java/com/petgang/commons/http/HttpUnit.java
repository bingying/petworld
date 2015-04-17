package com.petgang.commons.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class HttpUnit {

    private static CloseableHttpClient client = HttpClients.createDefault();

    private static RequestConfig REQUEST_CONFIG = RequestConfig.custom().setSocketTimeout(10000)
            .setConnectTimeout(2000).setRedirectsEnabled(true).setConnectionRequestTimeout(10000)
            .build();

    public static Result get(String uri) throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(uri);
        get.setConfig(REQUEST_CONFIG);
        CloseableHttpResponse response = client.execute(get);
        return buildResult(response);
    }

    public Result get(URI uri) throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(uri);
        get.setConfig(REQUEST_CONFIG);
        CloseableHttpResponse response = client.execute(get);
        return buildResult(response);
    }

    public Result get(String uri, Map<String, String> params) throws ClientProtocolException,
            IOException, URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(uri);
        for (Entry<String, String> tmp : params.entrySet()) {
            uriBuilder.addParameter(tmp.getKey(), tmp.getValue());
        }
        return get(uriBuilder.build());
    }

    public Result post(String uri) throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(uri);
        post.setConfig(REQUEST_CONFIG);
        CloseableHttpResponse response = client.execute(post);
        return buildResult(response);
    }

    /**
     * POST表单提交 参数内容放入Body
     * 
     * @param uri
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String uri, Map<String, String> params)
            throws ClientProtocolException, IOException {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            for (Entry<String, String> tmp : params.entrySet()) {
                parameters.add(new BasicNameValuePair(tmp.getKey(), tmp.getValue()));
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, Charsets.UTF_8);
        HttpPost post = new HttpPost(uri);
        post.setConfig(REQUEST_CONFIG);
        post.setEntity(entity);
        CloseableHttpResponse response = client.execute(post);
        return buildResult(response);
    }

    /**
     * 上传文件接口
     * 
     * @param uri
     * @param file
     * @param filename
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String uri, File file, String name, Map<String, String> params)
            throws ClientProtocolException, IOException {
        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put(name, file);
        return post(uri, fileMap, params);
    }

    /**
     * 上传文件
     * 
     * @param uri
     * @param fileMap 参数名<<<>>> 文件
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String uri, Map<String, File> fileMap, Map<String, String> params)
            throws ClientProtocolException, IOException {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setCharset(Charsets.UTF_8);
        for (Entry<String, File> tmp : fileMap.entrySet()) {
            entityBuilder.addBinaryBody(tmp.getKey(), tmp.getValue());
        }
        if (params != null && !params.isEmpty()) {
            ContentType contenttype = ContentType.create("text/plain", Charsets.UTF_8);
            for (Entry<String, String> tmp : params.entrySet()) {
                entityBuilder.addTextBody(tmp.getKey(), tmp.getValue(), contenttype);
            }
        }
        HttpPost post = new HttpPost(uri);
        post.setConfig(REQUEST_CONFIG);
        post.setEntity(entityBuilder.build());
        CloseableHttpResponse response = client.execute(post);
        return buildResult(response);
    }

    /**
     * 上传文件
     * 
     * @param uri
     * @param bytes
     * @param filename
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public Result post(String uri, byte[] bytes, String name, String filename,
            Map<String, String> params) throws ClientProtocolException, IOException {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setCharset(Charsets.UTF_8);
        entityBuilder.addBinaryBody("name", bytes, ContentType.MULTIPART_FORM_DATA, filename);
        if (params != null && !params.isEmpty()) {
            ContentType contenttype = ContentType.create("text/plain", Charsets.UTF_8);
            for (Entry<String, String> tmp : params.entrySet()) {
                entityBuilder.addTextBody(tmp.getKey(), tmp.getValue(), contenttype);
            }
        }
        HttpPost post = new HttpPost(uri);
        post.setConfig(REQUEST_CONFIG);
        post.setEntity(entityBuilder.build());
        CloseableHttpResponse response = client.execute(post);
        return buildResult(response);
    }

    private static Result buildResult(CloseableHttpResponse response) throws IOException {
        Result result = new Result();
        result.setCode(response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        entity.writeTo(bo);
        result.setBody(new String(bo.toByteArray(), "utf8"));
        response.close();
        return result;
    }
}
