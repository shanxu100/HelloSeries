
package org.helloseries.hellojava.chain.ok;

import org.helloseries.hellojava.chain.Request;
import org.helloseries.hellojava.chain.Response;

import java.util.LinkedList;
import java.util.List;

/**
 * 功能描述
 *
 * @since 2020-12-25
 */
public class Main {

    public static void main(String[] args) {

        List<Interceptor> list = new LinkedList<>();
        list.add(new DemoInterceptor());
        list.add(new FirstInterceptor());
        list.add(new SecondInterceptor());
        list.add(new FinalInterceptor());

        Interceptor.Chain chain = RealChain.newChain(list);

        int size = 1;
        Thread[] ts = new Thread[size];
        for (int i = 0; i < size; i++) {
            ts[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 创建一个 request
                    Request request = new Request();
                    // 使用责任链依次处理该request
                    Response response = chain.process(request);
                    System.out.println(Thread.currentThread().getName() + "" + response.toString());
                }
            });
        }

        for (int i = 0; i < size; i++) {
            ts[i].start();
        }

    }

    public static class FirstInterceptor implements Interceptor {
        @Override
        public Response interceptor(Request request, Chain next) {

            request.changeRequest("FirstInterceptor");

            Response response = next.process(request);
            response.changeResponse("FirstInterceptor");
            return response;
        }
    }

    public static class SecondInterceptor implements Interceptor {

        @Override
        public Response interceptor(Request request, Chain next) {

            request.changeRequest("SecondInterceptor");

            Response response = next.process(request);
            response.changeResponse("SecondInterceptor");
            return response;

        }
    }

    public static class FinalInterceptor implements Interceptor {
        @Override
        public Response interceptor(Request request, Chain next) {

            request.changeRequest("final");

            System.out.println(Thread.currentThread().getName() + "" + request.toString());

            Response response = next.process(request);

            if (response == null) {
                response = new Response();
                System.out
                    .println(Thread.currentThread().getName() + "" + "no more interceptor....create a new Response");
            }

            response.changeResponse("final");
            return response;

        }
    }

}
