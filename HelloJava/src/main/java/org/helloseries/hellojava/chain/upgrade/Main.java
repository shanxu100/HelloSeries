
package org.helloseries.hellojava.chain.upgrade;

import org.helloseries.hellojava.chain.Request;
import org.helloseries.hellojava.chain.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @since 2020-12-26
 */
public class Main {

    public static void main(String[] args) throws ChainException {

        List<Interceptor> list = new ArrayList<>();
        list.add(new FirstInterceptor());
        list.add(new DemoInterceptor());
        list.add(new FinalInterceptor());

        Request request = new Request();

        Interceptor.Chain chain = RealChain.newChain(list);

        // TODO 局限：只能处理一个request。当处理第二个request的时候，需要reset chain
        Response response = chain.process(request);

        System.out.println(response.toString());

    }

    private static final class FirstInterceptor implements Interceptor {

        @Override
        public Response interceptor(Request request, Chain chain) throws ChainException {
            request.changeRequest("upgrade FirstInterceptor");
            Response response = null;
            response = chain.process(request);

            response.changeResponse("upgrade FirstInterceptor");
            return response;
        }
    }

    private static final class FinalInterceptor implements Interceptor {

        @Override
        public Response interceptor(Request request, Chain chain) throws ChainException {
            request.changeRequest("upgrade FinalInterceptor");
            System.out.println(request.toString());

            Response response = null;
            try {
                response = chain.process(request);
            } catch (ChainException e) {
                // e.printStackTrace();
                response = new Response();
                System.out.println("no more interceptor....create a new Response");
            }

            response.changeResponse("upgrade FinalInterceptor");
            return response;
        }
    }

}
