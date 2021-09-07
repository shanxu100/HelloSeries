
package org.helloseries.hellorepo.task;

import java.util.concurrent.Executor;

/**
 * 功能描述 Hello task failed status notify.
 * 在HelloTaskImpl中的消息队列中的消息，Failed类型的消息
 * HelloTask notify failed handler in HelloTaskImpl ProcessQueue.
 * 负责对Failed的task进行通知
 */
class HelloTaskStatusNotifyOnFailure<TResult> implements IHelloTaskStatusNotify<TResult> {

    private final Executor mExecutor;

    private final Object mObj = new Object();

    private OnFailureListener mOnFailureListener;

    /**
     * Instantiates a new Hello task failed status notify.
     *
     * @param executor the executor
     * @param failureListener the failure listener
     */
    public HelloTaskStatusNotifyOnFailure(final Executor executor, final OnFailureListener failureListener) {
        mExecutor = executor;
        mOnFailureListener = failureListener;
    }

    /**
     * @param task which the status will be notified.
     */
    @Override
    public final void notifyStatus(final IHelloTask<TResult> task) {
        if (task.isSuccessful()) {
            // 成功的task，此处不作处理
            return;
        }
        synchronized (mObj) {
            if (mOnFailureListener == null) {
                return;
            }
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (mObj) {
                    if (mOnFailureListener != null) {
                        mOnFailureListener.onFailure(task.getException());
                    }
                }
            }
        });
    }
}