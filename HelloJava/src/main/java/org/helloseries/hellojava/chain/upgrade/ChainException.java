
package org.helloseries.hellojava.chain.upgrade;

/**
 * 功能描述
 *
 * @since 2020-12-26
 */
public class ChainException extends Exception {

    public ChainException() {
        super("no more interceptors to process");
    }

    public ChainException(String message) {
        super(message);
    }
}
