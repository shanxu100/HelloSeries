
package org.helloseries.hellojava.chain.upgrade;

import org.helloseries.hellojava.chain.Request;
import org.helloseries.hellojava.chain.Response;

import java.util.LinkedList;
import java.util.List;

/**
 * 功能描述
 *
 * @since 2020-12-26
 */
public class RealChain implements Interceptor.Chain {
    private List<Interceptor> interceptors = new LinkedList<>();

    private int index = 0;

    private RealChain(int index, List<Interceptor> interceptors) {
        this.index = index;
        this.interceptors = interceptors;
    }

    public static RealChain newChain(List<Interceptor> interceptors) {
        return new RealChain(0, interceptors);
    }

    @Override
    public Response process(Request request) throws ChainException {
        Interceptor interceptor = null;
        if (index >= interceptors.size()) {
            throw new ChainException();
        }
        interceptor = interceptors.get(index);
        index++;

        return interceptor.interceptor(request, this);
    }

}
