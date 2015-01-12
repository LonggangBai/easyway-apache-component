/**
 * 
 */
package com.easyway.apache.httpasynclient;

/**
 * @author longgangbai
 * 2015-1-12  下午12:07:26
 */

import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.DefaultHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
/**
 *  前段时间有个需求在springmvc
 * mapping的url跳转前完成一个统计的业务。显然需要进行异步的处理，不然出错或者异常会影响到后面的网页跳转。
 * 异步的方式也就是非阻塞式的，当异步调用成功与否程序会接着往下执行 ，不必等到输入输出处理完毕才返回。
 * 主要用到httpasyncclient-4.0.1.jar
 * ，httpclient-4.3.2.jar,httpcore-4.3.2.jar,httpcore
 * -nio-4.3.2.jar,commons-logging-1.1.3.jar。
 * java.util.concurrent中主要包括三类工具，Executor Freamework,并发集合（Concurrent
 * Collection），以及同步器（Synchronizer）。下面的例子是利用 java.util.concurrent.
 * Future只请求一个url异步请求。 Future接口表示异步计算的结果
 * 。它提供了检查计算是否完成的方法，以等待计算的完成，并获取计算的结果。计算完成后只能使用 get
 * 方法来获取结果，如有必要，计算完成前可以阻塞此方法。取消则由 cancel
 * 方法来执行。还提供了其他方法，以确定任务是正常完成还是被取消了。一旦计算完成，就不能再取消计算。如果为了可取消性而使用 Future
 * 但又不提供可用的结果，则可以声明 Future 形式类型、并返回 null 作为底层任务的结果。
 * 
 * @author longgangbai
 * 2015-1-12  下午12:29:16
 */
public class AsyncClientHttpExchange {

    public static void main(String[] args) throws Exception {
	CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();
	httpAsyncClient.start();
        try {
            HttpGet request = new HttpGet("http://www.apache.org/");
            Future<HttpResponse> future = httpAsyncClient.execute(request, null);
            HttpResponse response = future.get();
            System.out.println("Response: " + response.getStatusLine());
            System.out.println("Shutting down");
        } finally {
            httpAsyncClient.close();
        }
        System.out.println("Done");
    }

}
