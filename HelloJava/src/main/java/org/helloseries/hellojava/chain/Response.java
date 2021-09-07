
package org.helloseries.hellojava.chain;

/**
 * 功能描述
 *
 * @since 2020-12-25
 */
public class Response {

    private String msg = "Response:";

    public void changeResponse(String tail) {
        msg += tail + "->";
    }

    @Override
    public String toString() {
        return "Response{" + "msg='" + msg + '\'' + '}';
    }
}
