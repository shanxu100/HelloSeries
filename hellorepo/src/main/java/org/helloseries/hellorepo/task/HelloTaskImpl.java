
package org.helloseries.hellorepo.task;

import android.util.Log;

import java.util.concurrent.Executor;

/**
 * HelloTask implementation.
 * HelloTask的实现类
 */
public class HelloTaskImpl<TResult> implements IHelloTask<TResult> {

    private final Object mLock = new Object();

    /**
     * all process add in this queue if task is ok notify all processor in this queue
     * 维护一个处理的队列
     */
    private final HelloTaskStatusNotifyQueue<TResult> mProcessorQueue = new HelloTaskStatusNotifyQueue<>();

    /**
     * if the task is Completed client can get sessionID to monitor the installFromUnverifyIfNeed state
     * 该Task完成的标识
     */
    private boolean mIsTaskCompleted;

    /**
     * task result if the result is success the result is the sessionId
     * 当前Task的结果，客户端可以通过getResult进行获取
     */
    private TResult mResult;

    /**
     * task result if the task is failed 如果有异常情况下
     */
    private Exception mException;

    @Override
    public final boolean isComplete() {
        synchronized (mLock) {
            return mIsTaskCompleted;
        }
    }

    @Override
    public final boolean isSuccessful() {
        synchronized (mLock) {
            return mIsTaskCompleted && (mException == null);
        }
    }

    @Override
    public final TResult getResult() throws IllegalStateException, HelloRuntimeException {
        synchronized (mLock) {
            if (!mIsTaskCompleted) {
                throw new IllegalStateException("HelloTask is not yet complete");
            }
            if (mException != null) {
                throw new HelloRuntimeException(mException);
            }
            return mResult;
        }
    }

    @Override
    public <X extends Throwable> TResult getResult(Class<X> clazz) throws X, Throwable {
        synchronized (mLock) {
            if (!mIsTaskCompleted) {
                throw new IllegalStateException("HelloTask is not yet complete");
            }
            if (clazz.isInstance(mException)) {
                throw clazz.cast(mException);
            }
            if (mException != null) {
                throw new HelloRuntimeException(mException);
            }
            return mResult;
        }
    }

    @Override
    public final Exception getException() {
        synchronized (mLock) {
            return mException;
        }
    }

    @Override
    public final IHelloTask<TResult> addOnSuccessListener(final OnSuccessListener<? super TResult> onSuccessListener) {
        return addOnSuccessListener(HelloTaskExecutors.MAIN_THREAD, onSuccessListener);
    }

    @Override
    public final IHelloTask<TResult> addOnSuccessListener(final Executor executor,
        final OnSuccessListener<? super TResult> onSuccessListener) {
        Log.d("HelloTaskImpl", "addOnSuccessListener singleThread");
        mProcessorQueue.addTaskStatusNotifyItem(new HelloTaskStatusNotifyOnSuccess<>(executor, onSuccessListener));
        handleQueueIfCompleted();
        return this;
    }

    @Override
    public final IHelloTask<TResult> addOnFailureListener(final OnFailureListener onFailureListener) {
        return addOnFailureListener(HelloTaskExecutors.MAIN_THREAD, onFailureListener);
    }

    @Override
    public final IHelloTask<TResult> addOnFailureListener(final Executor executor,
        final OnFailureListener onFailureListener) {
        mProcessorQueue
            .addTaskStatusNotifyItem(new HelloTaskStatusNotifyOnFailure<TResult>(executor, onFailureListener));
        handleQueueIfCompleted();
        return this;
    }

    @Override
    public final IHelloTask<TResult> addOnCompleteListener(final OnCompleteListener<TResult> onCompleteListener) {
        return addOnCompleteListener(HelloTaskExecutors.MAIN_THREAD, onCompleteListener);
    }

    @Override
    public final IHelloTask<TResult> addOnCompleteListener(final Executor executor,
        final OnCompleteListener<TResult> onCompleteListener) {
        mProcessorQueue.addTaskStatusNotifyItem(new HelloTaskStatusNotifyOnComplete<>(executor, onCompleteListener));
        handleQueueIfCompleted();
        return this;
    }

    /**
     * Notified Result when there is a Error throws the Exception.
     *
     * @param result the result
     * @throws IllegalStateException the illegal state exception
     */
    final void notifyResultThrowException(final TResult result) throws IllegalStateException {
        Log.d("HelloTaskImpl", "notifyResultThrowException result " + result);
        synchronized (mLock) {
            if (mIsTaskCompleted) {
                throw new IllegalStateException("HelloTask is already complete");
            }
            mIsTaskCompleted = true;
            mResult = result;
        }
        mProcessorQueue.notify(this);
    }

    /**
     * Notify result with return value not throws the exception.
     *
     * @param result the result
     * @return the boolean
     */
    public final boolean notifyResult(final TResult result) {
        Log.d("HelloTaskImpl", "notifyResult result " + result);
        synchronized (mLock) {
            if (mIsTaskCompleted) {
                return false;
            }
            mIsTaskCompleted = true;
            mResult = result;
        }
        mProcessorQueue.notify(this);
        return true;
    }

    /**
     * 设置当前的Task的异常
     *
     * @param exception the exception
     */
    public final void notifyFailureIfException(final Exception exception) {
        if (exception == null) {
            throw new IllegalArgumentException("Exception must not be null");
        }
        synchronized (mLock) {
            if (mIsTaskCompleted) {
                throw new IllegalStateException("HelloTask is already complete");
            }
            mIsTaskCompleted = true;
            mException = exception;
        }
        mProcessorQueue.notify(this);
    }

    /**
     * 设置当前的Task的异常
     *
     * @param exception the exception
     * @return boolean boolean
     */
    public final boolean notifyFailureException(final Exception exception) {
        if (exception == null) {
            throw new IllegalArgumentException("Exception must not be null");
        }
        synchronized (mLock) {
            if (mIsTaskCompleted) {
                return false;
            }
            mIsTaskCompleted = true;
            mException = exception;
        }
        mProcessorQueue.notify(this);
        return true;
    }

    /**
     * notify all the listener added in the Queue.
     */
    private void handleQueueIfCompleted() {
        synchronized (mLock) {
            if (!mIsTaskCompleted) {
                return;
            }
        }
        mProcessorQueue.notify(this);
    }
}
