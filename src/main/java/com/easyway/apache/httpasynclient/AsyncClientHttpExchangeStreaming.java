/**
 * 
 */
package com.easyway.apache.httpasynclient;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.protocol.HttpContext;

/**
 * @author longgangbai 2015-1-12 下午12:10:06
 */
public class AsyncClientHttpExchangeStreaming {

    public static void main(String[] args) throws Exception {
	CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();
	httpAsyncClient.start();
	try {
	    Future<Boolean> future = httpAsyncClient.execute(HttpAsyncMethods.createGet("http://hao.360.cn/"), new MyResponseConsumer(), null);
	    Boolean result = future.get();
	    if (result != null && result.booleanValue()) {
		System.out.println("Request successfully executed");
	    } else {
		System.out.println("Request failed");
	    }
	    System.out.println("Shutting down");
	} finally {
	    httpAsyncClient.close();
	}
	System.out.println("Done");
    }

    static class MyResponseConsumer extends AsyncCharConsumer<Boolean> {

	@Override
	protected void onResponseReceived(final HttpResponse response) {
	}

	@Override
	protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
	    while (buf.hasRemaining()) {
		System.out.print(buf.get());
	    }
	}

	@Override
	protected void releaseResources() {
	}

	@Override
	protected Boolean buildResult(final HttpContext context) {
	    return Boolean.TRUE;
	}

    }

}