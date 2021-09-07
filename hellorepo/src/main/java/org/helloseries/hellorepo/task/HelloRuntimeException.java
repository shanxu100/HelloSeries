
package org.helloseries.hellorepo.task;

/**
 * 功能描述 Hello runtime execution exception.
 * HelloTask执行的自定义异常
 */
public class HelloRuntimeException extends RuntimeException {

    /**
     * Instantiates a new Hello runtime execution exception.
     *
     * @param throwable the throwable
     */
    public HelloRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
