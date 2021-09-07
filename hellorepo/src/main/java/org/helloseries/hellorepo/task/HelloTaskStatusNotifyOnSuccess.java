

package org.helloseries.hellorepo.task;

import java.util.concurrent.Executor;

/**
 * 功能描述 Hello task successed status notify.
 * 在HelloTaskImpl中的消息队列中的消息，Success类型的消息
 * HelloTask notify sucess handler in HelloTaskImpl ProcessQueue.
 */
class HelloTaskStatusNotifyOnSuccess<TResult> implements IHelloTaskStatusNotify<TResult> {

    private final Executor mExecutor;

    private final Object mLock = new Object();

    private OnSuccessListener<? super TResult> mOnSuccessListener;

    /**
     * Instantiates a new Hello task successed status notify.
     *
     * @param executor the executor
     * @param onSuccessListener the on feature success listener
     */
    public HelloTaskStatusNotifyOnSuccess(final Executor executor,
        final OnSuccessListener<? super TResult> onSuccessListener) {
        mExecutor = executor;
        mOnSuccessListener = onSuccessListener;
    }

    @Override
    public void notifyStatus(final IHelloTask<TResult> task) {
        if (!task.isSuccessful()) {
            // 不成功的task，此处不处理
            return;
        }
        synchronized (mLock) {
            if (mOnSuccessListener == null) {
                return;
            }
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (mLock) {
                    if (mOnSuccessListener != null) {
                        mOnSuccessListener.onSuccess(task.getResult());
                    }
                }
            }
        });
    }
}
