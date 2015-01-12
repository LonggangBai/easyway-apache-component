package com.easyway.apache.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author longgangbai
 * 2015-1-12  下午12:41:25
 */
public class HttpUtils {

    private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static int CONN_TIMEOUT = 3000;

    private static int READ_TIMEOUT = 10000;

    /**
     * HTTP GET请求，返回请求响应信息
     * 
     * @param url
     * @param queryString
     * @param charset
     * @param pretty
     * @return
     */
    public static String doGet(String url, String queryString, String charset, boolean pretty) {
	StringBuilder response = new StringBuilder();
	HttpEntity httpentity = null;
	HttpClient httpclient = HttpClients.createDefault();
	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONN_TIMEOUT).build();
	HttpGet method = new HttpGet(url);
	method.setConfig(requestConfig);
	try {
	    if (queryString!=null) {
		method.setURI(URIUtils.resolve(method.getURI(), queryString));
	    }
	    logger.info("访问：" + url + "?" + queryString);
	    if (logger.isDebugEnabled()) {
		logger.debug("请求地址：" + url);
		logger.debug(" 请求参数：" + queryString);
	    }
	    HttpResponse httpResponse = httpclient.execute(method);
	    httpentity = httpResponse.getEntity();
	    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpentity.getContent(), charset));
		String line = null;
		while ((line = reader.readLine()) != null) {
		    if (pretty)
			response.append(line).append(System.getProperty("line.separator"));
		    else
			response.append(line);
		}
		reader.close();
	    } else {
		logger.error("访问失败：" + httpResponse.getStatusLine().getStatusCode());
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpentity.getContent(), charset));
		String line = null;
		StringBuffer errorBuffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
		    errorBuffer.append(line);
		}
		reader.close();
		logger.error(errorBuffer.toString());
		response.append("AccessFailed");
	    }
	} catch (Exception e) {
	    logger.error(e.getMessage(), e);
	    response.append("AccessFailed");
	}
	return response.toString();
    }

    public static String doPost(String url, Map<String, String> params, String charset, boolean pretty) {
	StringBuilder response = new StringBuilder();
	long start = System.currentTimeMillis();
	HttpClient httpclient = HttpClients.createDefault();
	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONN_TIMEOUT).build();
	HttpPost method = new HttpPost(url);
	method.setConfig(requestConfig);
	List<NameValuePair> nvps = new ArrayList<NameValuePair>(params.size());
	if (params != null) {
	    for (Map.Entry<String, String> entry : params.entrySet()) {
		nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	    }
	}
	try {
	    method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); // 将参数传入post方法中
	    if (logger.isDebugEnabled()) {
		logger.debug("请求地址：" + url);
		logger.debug(" 请求参数：" + params.toString());
	    }
	    HttpResponse resp = httpclient.execute(method);
	    HttpEntity entity = resp.getEntity();
	    if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
		String line = null;
		while ((line = reader.readLine()) != null) {
		    if (pretty)
			response.append(line).append(System.getProperty("line.separator"));
		    else
			response.append(line);
		}
		reader.close();
	    } else {
		logger.error(String.valueOf(resp.getStatusLine().getStatusCode()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
		String line = null;
		while ((line = reader.readLine()) != null) {
		    logger.error(line);
		}
		reader.close();
		response.append("AccessFailed");
	    }
	} catch (Exception e) {
	    logger.error(e.getMessage(), e);
	    response.append("AccessFailed");
	}
	long end = System.currentTimeMillis();
	System.out.println("请求业务中心服务" + url + "耗时" + (end - start) + " ms");
	return response.toString();
    }

}
