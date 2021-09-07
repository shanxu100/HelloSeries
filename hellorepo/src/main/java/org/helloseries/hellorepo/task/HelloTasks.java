
package org.helloseries.hellorepo.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * HelloTask的工具方法
 */
public class HelloTasks {

    private static final String TAG = "HelloTasks";

    /**
     * 对Task设置Result, 返回值来标识
     * this is used for start install return result.
     * but it is not open for developer.
     *
     * @param <TResult> the type parameter
     * @param result the result
     * @return feature task
     */
    public static <TResult> IHelloTask<TResult> createHelloTask(TResult result) {
        HelloTaskImpl<TResult> featureTaskImpl = new HelloTaskImpl<>();
        featureTaskImpl.notifyResult(result);
        return featureTaskImpl;
    }

    /**
     * 根据当前的Exception，来设置Task
     *
     * @param <TResult> the type parameter
     * @param exception 异常
     * @return feature task
     */
    public static <TResult> IHelloTask<TResult> createHelloTask(final Exception exception) {
        HelloTaskImpl<TResult> featureTaskImpl = new HelloTaskImpl<>();
        featureTaskImpl.notifyFailureException(exception);
        return featureTaskImpl;
    }

    /**
     * Blocks until the specified HelloTask is complete.
     * 阻塞的等待task的完成
     *
     * @param <TResult> the type parameter
     * @param task the task
     * @return the HelloTask's result
     * @throws ExecutionException the execution exception
     * @throws InterruptedException the interrupted exception
     */
    public static <TResult> TResult await(final IHelloTask<TResult> task)
        throws ExecutionException, InterruptedException {
        if (task == null) {
            throw new IllegalArgumentException("HelloTask must not be null");
        }
        if (task.isComplete()) {
            return getResult(task);
        }
        CountDownListener countDownListener = new CountDownListener();
        registerCountDownLatchListener(task, countDownListener);
        countDownListener.await();
        return getResult(task);
    }

    /**
     * Blocks until the specified HelloTask is complete.
     *
     * @param <TResult> the type parameter
     * @param HelloTask the feature task
     * @param time the time
     * @param timeUnit the time unit
     * @return the HelloTask's result
     * @throws TimeoutException if the specified timeout is reached before the HelloTask completes.
     * @throws ExecutionException if the HelloTask is failed.
     * @throws InterruptedException if an interrupt occurs while waiting for the HelloTask to complete.
     */
    public static <TResult> TResult await(IHelloTask<TResult> HelloTask, long time, TimeUnit timeUnit)
        throws TimeoutException, ExecutionException, InterruptedException {
        if (HelloTask == null || timeUnit == null) {
            throw new IllegalArgumentException("HelloTask or timeUnit must not be null");
        }
        if (HelloTask.isComplete()) {
            return getResult(HelloTask);
        } else {
            CountDownListener countDownListener = new CountDownListener();
            registerCountDownLatchListener(HelloTask, countDownListener);
            if (!countDownListener.await(time, timeUnit)) {
                throw new TimeoutException("Timed out waiting for HelloTask");
            }
            return getResult(HelloTask);
        }
    }

    private static <TResult> TResult getResult(final IHelloTask<TResult> HelloTask) throws ExecutionException {
        if (HelloTask.isSuccessful()) {
            return HelloTask.getResult();
        }
        throw new ExecutionException(HelloTask.getException());
    }

    private static void registerCountDownLatchListener(final IHelloTask<?> task,
        final CountDownListener countDownListener) {
        task.addOnSuccessListener(HelloTaskExecutors.EXECUTOR, countDownListener);
        task.addOnFailureListener(HelloTaskExecutors.EXECUTOR, countDownListener);
    }

    /**
     * CountDownLatch for HelloTask wait action.
     *
     * @since 2019 /09/25
     */
    static class CountDownListener implements OnFailureListener, OnSuccessListener<Object> {

        private final CountDownLatch mCountDownLatch = new CountDownLatch(1);

        @Override
        public final void onSuccess(final Object result) {
            android.util.Log.d(TAG, "CountDownListenerHelloHello onSuccess");
            mCountDownLatch.countDown();
        }

        @Override
        public final void onFailure(final Exception exception) {
            android.util.Log.d(TAG, "CountDownListenerHelloHello onFailure");
            mCountDownLatch.countDown();
        }

        /**
         * Await.
         *
         * @throws InterruptedException the interrupted exception
         */
        public final void await() throws InterruptedException {
            mCountDownLatch.await();
        }

        /**
         * Await boolean.
         *
         * @param secs the secs
         * @param unit the unit
         * @return the boolean
         * @throws InterruptedException the interrupted exception
         */
        public final boolean await(final long secs, final TimeUnit unit) throws InterruptedException {
            return mCountDownLatch.await(secs, unit);
        }
    }
}
