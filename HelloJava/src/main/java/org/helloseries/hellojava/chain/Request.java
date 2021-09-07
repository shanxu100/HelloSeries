
package org.helloseries.hellojava.chain;

/**
 * 功能描述
 *
 * @since 2020-12-25
 */
public class Request {

    String msg = "Request:";

    public void changeRequest(String tail) {
        msg += tail + "->";
    }

    @Override
    public String toString() {
        return "Request{" + "msg='" + msg + '\'' + '}';
    }
}
