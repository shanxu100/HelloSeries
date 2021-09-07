
package org.helloseries.hellorepo.task;

import java.util.concurrent.Executor;

/**
 * 功能描述 Hello task completed status notify.
 * 在HelloTaskImpl中的消息队列中的消息，Completed类型的消息
 * HelloTask notify completed listener in HelloTaskImpl ProcessQueue.
 * 负责对Completed的task进行通知
 */
class HelloTaskStatusNotifyOnComplete<TResult> implements IHelloTaskStatusNotify<TResult> {

    private final Executor mExecutor;

    private final Object mLock = new Object();

    private OnCompleteListener<TResult> mCompleteListener;

    /**
     * 需要传入当前客户端指定的线程和监听
     *
     * @param executor the executor
     * @param onCompleteListener the on feature complete listener
     */
    public HelloTaskStatusNotifyOnComplete(final Executor executor,
        final OnCompleteListener<TResult> onCompleteListener) {
        mExecutor = executor;
        mCompleteListener = onCompleteListener;
    }

    @Override
    public void notifyStatus(final IHelloTask<TResult> task) {
        synchronized (mLock) {
            if (mCompleteListener == null) {
                return;
            }
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (mLock) {
                    if (mCompleteListener != null) {
                        mCompleteListener.onComplete(task);
                    }
                }
            }
        });
    }
}
