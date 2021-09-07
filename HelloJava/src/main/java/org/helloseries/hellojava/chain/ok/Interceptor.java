
package org.helloseries.hellojava.chain.ok;

import org.helloseries.hellojava.chain.Request;
import org.helloseries.hellojava.chain.Response;

/**
 * Interceptor 拦截器接口定义
 * 处理 request 并返回 response 的主体
 *
 * @since 2020-12-25
 */
public interface Interceptor {

    /**
     * 做具体的 interceptor 处理逻辑
     * 如果需要下一个interceptor继续处理，则调用 chain.process 获取response
     * 否则，自己创建一个response返回
     *
     * @param request request 上一个 interceptor 处理后的 request，即本interceptor可以处理的request
     * @param next 责任链中的下一个处理环节。即调用 response = next.process(request) 方法，
     *        可以将本interceptor可以处理完的request传递给下一环节，并获取从下一环节返回的response结果
     * @return response 本interceptor处理完后的response结果
     */
    Response interceptor(Request request, Chain next);

    /**
     * 责任链中的一个环节
     * 一条责任链的运行需要创建多个Chain对象
     */
    interface Chain {

        /**
         * 1、获取当前 interceptor，将 request 交给interceptor处理
         * 2、返回当前interceptor的 response 结果
         *
         * @param request 当前 interceptor 需要处理的 request
         * @return 当前 interceptor 处理的 response 结果
         */
        Response process(Request request);

    }

}
