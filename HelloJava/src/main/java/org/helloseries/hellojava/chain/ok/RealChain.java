
package org.helloseries.hellojava.chain.ok;

import org.helloseries.hellojava.chain.Request;
import org.helloseries.hellojava.chain.Response;

import java.util.List;

/**
 * 责任链模式
 * chain表示链中的某一个环节
 * 线程安全、无需重置就可多次process
 *
 * @since 2020-12-25
 */
public class RealChain implements Interceptor.Chain {

    private List<Interceptor> interceptors;

    private int index;

    private RealChain(List<Interceptor> interceptors, int index) {
        this.interceptors = interceptors;
        this.index = index;
    }

    /**
     * 创建一个chain对象，且index =0，即该处于整个责任链的第一个环节的chain
     *
     * @param interceptors 拦截器list
     * @return chain对象
     */
    public static Interceptor.Chain newChain(List<Interceptor> interceptors) {
        return new RealChain(interceptors, 0);
    }

    @Override
    public Response process(Request request) {

        if (index >= interceptors.size()) {
            // TODO 此处没有更多的 interceptor ，需要抛异常
            return null;
        }

        Interceptor interceptor = interceptors.get(index);

        Interceptor.Chain next = new RealChain(interceptors, index + 1);

        return interceptor.interceptor(request, next);
    }
}
