/**
 * 
 */
package com.easyway.apache.httpclient;

/**
 * @author longgangbai
 * 2015-1-12  下午12:53:03
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author amosli 登录并抓取中国联通数据
 */

public class LoginChinaUnicom {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

	String name = "中国联通手机号码";
	String pwd = "手机服务密码";

	String url = "https://uac.10010.com/portal/Service/MallLogin?callback=jQuery17202691898950318097_1403425938090&redirectURL=http%3A%2F%2Fwww.10010.com&userName="
		+ name + "&password=" + pwd + "&pwdType=01&productType=01&redirectType=01&rememberMe=1";

	HttpClient httpClient = HttpClients.createDefault();
	HttpGet httpGet = new HttpGet(url);
	HttpResponse loginResponse = httpClient.execute(httpGet);

	if (loginResponse.getStatusLine().getStatusCode() == 200) {
	    for (Header head : loginResponse.getAllHeaders()) {
		System.out.println(head);
	    }
	    HttpEntity loginEntity = loginResponse.getEntity();
	    String loginEntityContent = EntityUtils.toString(loginEntity);
	    System.out.println("登录状态:" + loginEntityContent);
	    // 如果登录成功
	    if (loginEntityContent.contains("resultCode:\"0000\"")) {

		// 月份
		String months[] = new String[] { "201401", "201402", "201403", "201404", "201405" };

		for (String month : months) {
		    String billurl = "http://iservice.10010.com/ehallService/static/historyBiil/execute/YH102010002/QUERY_YH102010002.processData/QueryYH102010002_Data/"
			    + month + "/undefined";

		    HttpPost httpPost = new HttpPost(billurl);
		    HttpResponse billresponse = httpClient.execute(httpPost);

		    if (billresponse.getStatusLine().getStatusCode() == 200) {
			saveToLocal(billresponse.getEntity(), "chinaunicom.bill." + month + ".2.html");
		    }
		}
	    }
	}
    }

    /**
     * 写文件到本地
     * 
     * @param httpEntity
     * @param filename
     */
    public static void saveToLocal(HttpEntity httpEntity, String filename) {

	try {

	    File dir = new File("D:/chinaunicom/");
	    if (!dir.isDirectory()) {
		dir.mkdir();
	    }

	    File file = new File(dir.getAbsolutePath() + "/" + filename);
	    FileOutputStream fileOutputStream = new FileOutputStream(file);
	    InputStream inputStream = httpEntity.getContent();

	    if (!file.exists()) {
		file.createNewFile();
	    }
	    byte[] bytes = new byte[1024];
	    int length = 0;
	    while ((length = inputStream.read(bytes)) > 0) {
		fileOutputStream.write(bytes, 0, length);
	    }
	    inputStream.close();
	    fileOutputStream.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }
}