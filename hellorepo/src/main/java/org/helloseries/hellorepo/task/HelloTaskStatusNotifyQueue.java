
package org.helloseries.hellorepo.task;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 功能描述 Hello task status notify queue.
 * 通知的task信息的消息队列，当task结束的时候，进行结果的分发
 * HelloTask Queue for message handle.
 */
final class HelloTaskStatusNotifyQueue<TResult> {

    private final Object mLock = new Object();

    private Queue<IHelloTaskStatusNotify<TResult>> mQueue;

    private boolean mIsNotified;

    /**
     * 队列中增加通知信息
     *
     * @param ITaskStatusNotify the task status notify
     */
    public void addTaskStatusNotifyItem(final IHelloTaskStatusNotify<TResult> ITaskStatusNotify) {
        synchronized (mLock) {
            if (mQueue == null) {
                mQueue = new ArrayDeque<>();
            }
            mQueue.add(ITaskStatusNotify);
        }
    }

    /**
     * 通知所有的消息结果分到到所有的listener
     *
     * @param task task结果
     */
    public final void notify(final IHelloTask<TResult> task) {
        synchronized (mLock) {
            if (mQueue == null || mIsNotified) {
                return;
            }
            mIsNotified = true;
        }
        while (true) {
            final IHelloTaskStatusNotify<TResult> notifier;
            synchronized (mLock) {
                if ((notifier = mQueue.poll()) == null) {
                    mIsNotified = false;
                    return;
                }
            }
            notifier.notifyStatus(task);
        }
    }
}
