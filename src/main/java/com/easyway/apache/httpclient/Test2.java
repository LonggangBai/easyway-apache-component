/**
 * 
 */
package com.easyway.apache.httpclient;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * @author longgangbai 2015-1-12 下午12:37:57
 */
public class Test2 {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();      //实例化一个HttpClient
        HttpResponse response = null;
        HttpEntity entity = null;
        httpclient.getParams().setParameter(
                ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);  //设置cookie的兼容性
        HttpPost httpost = new HttpPost("http://127.0.0.1:8080/pub/jsp/getInfo");           //引号中的参数是：servlet的地址
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();                     
        nvps.add(new BasicNameValuePair("jqm", "fb1f7cbdaf2bf0a9cb5d43736492640e0c4c0cd0232da9de"));  
        //   BasicNameValuePair("name", "value"), name是post方法里的属性, value是传入的参数值
        nvps.add(new BasicNameValuePair("sqm", "1bb5b5b45915c8"));
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));            //将参数传入post方法中
        response = httpclient.execute(httpost);                                               //执行
        entity = response.getEntity();                                                             //返回服务器响应
        try{
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());                           //服务器返回状态
            Header[] headers = response.getAllHeaders();                    //返回的HTTP头信息
            for (int i=0; i<headers.length; i++) {                              
            System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");
            String responseString = null;
            if (response.getEntity() != null) {
            responseString = EntityUtils.toString(response.getEntity());      //返回服务器响应的HTML代码
            System.out.println(responseString);                                   //打印出服务器响应的HTML代码
            }
        } finally {
            if (entity != null)                          
            entity.consumeContent();                                                   // release connection gracefully
        }
        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
        entity.consumeContent();
        }
       
    }
}
