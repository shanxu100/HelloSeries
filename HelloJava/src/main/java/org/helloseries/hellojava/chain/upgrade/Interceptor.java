
package org.helloseries.hellojava.chain.upgrade;

import org.helloseries.hellojava.chain.Request;
import org.helloseries.hellojava.chain.Response;

/**
 * 功能描述
 *
 * @since 2020-12-25
 */
public interface Interceptor {

    Response interceptor(Request request, Chain chain) throws ChainException;

    interface Chain {

        Response process(Request request) throws ChainException;

    }

}
