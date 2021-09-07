
package org.helloseries.hellojava.chain.ok;

import org.helloseries.hellojava.chain.Request;
import org.helloseries.hellojava.chain.Response;

/**
 * 功能描述
 *
 * @since 2020-12-26
 */
public class DemoInterceptor implements Interceptor {
    @Override
    public Response interceptor(Request request, Chain next) {
        request.changeRequest("DemoInterceptor");
        Response response = next.process(request);
        response.changeResponse("DemoInterceptor");
        return response;

    }
}
