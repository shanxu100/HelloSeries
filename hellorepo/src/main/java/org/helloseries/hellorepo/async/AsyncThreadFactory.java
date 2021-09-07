
package org.helloseries.hellorepo.async;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 功能描述
 *
 * @since 2021-03-17
 */
public class AsyncThreadFactory implements ThreadFactory {
    private final ThreadGroup group;

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String namePrefix;

    private final int priority;

    public AsyncThreadFactory(String threadType, int priority) {
        this.priority = priority;
        group = Thread.currentThread().getThreadGroup();
        namePrefix = threadType + "-pool-thread-";
    }

    public AsyncThreadFactory(String threadType) {
        this(threadType, Thread.NORM_PRIORITY);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != priority) {
            t.setPriority(priority);
        }
        return t;
    }
}
