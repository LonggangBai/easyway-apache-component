/**
 * 
 */
package com.easyway.apache.httpclient;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author longgangbai 2015-1-12 下午12:57:19
 */
public class HttpFileUtils {

    public static String upload(String url, File f, String fileName, String contentType) throws ClientProtocolException, IOException {
	CloseableHttpClient httpclient = HttpClients.createDefault();
	String result = null;
	try {
	    if (contentType == null) {
		contentType = "image/pjpeg";
	    }
	    HttpPost httppost = new HttpPost(url);
	    FileEntity reqEntity = new FileEntity(f, ContentType.create(contentType));
	    httppost.setEntity(reqEntity);
	    printLog(httppost.getRequestLine().toString());
	    CloseableHttpResponse response = httpclient.execute(httppost);
	    try {
		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
		    printLog("Responseh: " + resEntity.getContentLength());
		    result = EntityUtils.toString(resEntity);
		    printLog("Response: " + result);

		}
	    } finally {
		response.close();
	    }
	} finally {
	    httpclient.close();
	}
	return result;
    }

    private static void printLog(String name) {
	System.out.println(name);
    }
}
