package com.petgang.commons.http;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.Charsets;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

public class HttpUnit {

    private static CloseableHttpClient client = null;
    static {
        BasicHttpClientConnectionManager manager = new BasicHttpClientConnectionManager();
        manager.closeIdleConnections(5, TimeUnit.SECONDS);
        client = HttpClients.createMinimal(manager);
    }

    public Result get(String uri) throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = client.execute(get);
        return Result.getInstance(response);
    }

    public Result get(URI uri) throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = client.execute(get);
        return Result.getInstance(response);
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
        CloseableHttpResponse response = client.execute(post);
        return Result.getInstance(response);
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
        post.setEntity(entity);
        CloseableHttpResponse response = client.execute(post);
        return Result.getInstance(response);
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
        post.setEntity(entityBuilder.build());
        CloseableHttpResponse response = client.execute(post);
        return Result.getInstance(response);
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
        post.setEntity(entityBuilder.build());
        CloseableHttpResponse response = client.execute(post);
        return Result.getInstance(response);
    }

}
