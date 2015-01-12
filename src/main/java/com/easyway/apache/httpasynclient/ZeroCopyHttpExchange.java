/**
 * 
 */
package com.easyway.apache.httpasynclient;


import java.io.File;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.methods.ZeroCopyConsumer;
import org.apache.http.nio.client.methods.ZeroCopyPost;
/**
 * @author longgangbai
 * 2015-1-12  下午12:33:05
 */
public class ZeroCopyHttpExchange {

    public static void main(String[] args) throws Exception {
	CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
	httpclient.start();
        try {
            File upload = new File(args[0]);
            File download = new File(args[1]);
            ZeroCopyPost httpost = new ZeroCopyPost("http://localhost:8080/", upload,
                    ContentType.create("text/plain"));
            ZeroCopyConsumer<File> consumer = new ZeroCopyConsumer<File>(download) {

                @Override
                protected File process(
                        final HttpResponse response, 
                        final File file,
                        final ContentType contentType) throws Exception {
                    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                        throw new ClientProtocolException("Upload failed: " + response.getStatusLine());
                    }
                    return file;
                }

            };
            Future<File> future = httpclient.execute(httpost, consumer, null);
            File result = future.get();
            System.out.println("Response file length: " + result.length());
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

}