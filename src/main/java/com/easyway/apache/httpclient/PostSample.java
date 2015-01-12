/**
 * 
 */
package com.easyway.apache.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * @author longgangbai 2015-1-12 下午12:45:50
 */
public class PostSample {
    public static void main(String[] args) throws ParseException, IOException {
	String url = "http://www.g.cn/";
	System.out.println(url);
	System.out.println("Visit google using Apache commons-httpclient 3.1：");
	List<NameValuePair> data3 = new ArrayList<NameValuePair>();
	data3.add(new BasicNameValuePair("username", "testuser"));
	data3.add(new BasicNameValuePair("password", "testpassword"));
	System.out.println(post3(url, data3));
	System.out.println("Visit google using Apache HttpComponents Client 4.0：");
	List<BasicNameValuePair> data4 = new ArrayList<BasicNameValuePair>();
	data4.add(new BasicNameValuePair("username", "testuser"));
	data4.add(new BasicNameValuePair("password", "testpassword"));
	System.out.println(post4(url, data4));
    }

    /** 使用Apache commons-httpclient 3.1，POST方法访问网页 */
    public static String post3(String url, List<NameValuePair> data) throws IOException {
	HttpClient httpClient = HttpClients.createDefault();
	HttpPost postMethod = new HttpPost(url);
	postMethod.setEntity(new UrlEncodedFormEntity(data));
	try {
	    System.out.println("<< Response: " + httpClient.execute(postMethod));
	    return EntityUtils.toString(postMethod.getEntity());
	} finally {
	    postMethod.releaseConnection();
	}
    }

    /** 使用Apache HttpComponents Client 4.0，POST方法访问网页 */
    private static String post4(String url, List<? extends org.apache.http.NameValuePair> data) throws ParseException, IOException {
	org.apache.http.client.HttpClient client = HttpClients.createDefault();
	HttpPost httpost = new HttpPost(url);
	httpost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
	try {
	    HttpResponse response = client.execute(httpost);
	    HttpEntity entity = response.getEntity();
	    System.out.println("<< Response: " + response.getStatusLine());
	    if (entity != null) {
		return EntityUtils.toString(entity);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return null;
    }
}