/**
 * 
 */
package com.easyway.apache.httpasynclient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

/**
 * * This example demonstrates a fully asynchronous execution of multiple HTTP
 * exchanges where the result of an individual operation is reported using a
 * callback interface.
 * 
 * 
 * 同步器 （Synchronizer）是一些使线程能够等待另一个线程的对象，允许它们协作，最常用的同步器是CountDownLatch和Semaphore。
 * 较不常用的是CyclicBarrier和Exchanger。 Semaphore类是一个计数信号量。从概念上讲，信号量维护了一个许可集。如有必要，
 * 在许可可用前会阻塞每一个 acquire()，然后再获取该许可。每个 release() 添加一个许可，从而可能释放一个正在阻塞的获取者。
 * 但是，不使用实际的许可对象，Semaphore 只对可用许可的号码进行计数，并采取相应的行动。
 * CountDownLatch是一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。 用给定的计数 初始化
 * CountDownLatch。由于调用了 countDown() 方法，所以在当前计数到达零之前，await 方法会一直受阻塞。
 * 之后，会释放所有等待的线程，await 的所有后续调用都将立即返回。这种现象只出现一次——计数无法被重置。如果需要重置计数， 可使用
 * CyclicBarrier。倒计数索存器（ CountDownLatch）是一次性的障碍。它的唯一构造器带有一个int类型的参数，
 * 这个参数是指允许所有的等待线程处理之前，必须在锁存器上调用countDown()方法的次数。这一点非常有用。 下面是异步请求一组url的例子，利用
 * callback借口完成独立的操作。
 * 
 * @author longgangbai 2015-1-12 下午12:13:02
 */
public class AsyncClientHttpExchangeFutureCallback {
    public static void main(final String[] args) throws Exception {
	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
	CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
	try {
	    httpclient.start();
	    final HttpGet[] requests = new HttpGet[] { new HttpGet("http://www.apache.org/"), new HttpGet("https://www.verisign.com/"),
		    new HttpGet("http://www.google.com/"), new HttpGet("http://www.baidu.com/") };
	    long start = System.currentTimeMillis();
	    final CountDownLatch latch = new CountDownLatch(requests.length);
	    for (final HttpGet request : requests) {
		Future<HttpResponse> future = httpclient.execute(request, new FutureCallback<HttpResponse>() {
		    // 无论完成还是失败都调用countDown()
		    public void cancelled() {
			latch.countDown();
			System.out.println(request.getRequestLine() + " cancelled");
		    }

		    public void completed(HttpResponse response) {
			latch.countDown();
			System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
		    }

		    public void failed(Exception ex) {
			latch.countDown();
			System.out.println(request.getRequestLine() + "->" + ex);
		    }

		});
//		HttpResponse httpResponse = future.get();
//
//		String entity = EntityUtils.toString(httpResponse.getEntity());
//		System.out.println("entry=" + entity);

	    }
	    latch.await();
	    long end = System.currentTimeMillis();

	    System.out.println("Shutting down=" + (end - start));
	} finally {
	    httpclient.close();
	}
	System.out.println("Done");
    }
}