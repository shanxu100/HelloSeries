
package org.helloseries.hellorepo.task;

/**
 * 功能描述 Hello task holder.
 * HelloTask的holder
 */
public class HelloTaskHolder<TResult> {

    private final HelloTaskImpl<TResult> mTaskImpl;

    /**
     * Instantiates a new Hello task holder.
     */
    public HelloTaskHolder() {
        mTaskImpl = new HelloTaskImpl<>();
    }

    /**
     * 设置内置task的Result
     *
     * @param result the result
     * @return boolean
     */
    public final boolean notifyResult(TResult result) {
        return mTaskImpl.notifyResult(result);
    }

    /**
     * 设置内置task的失败异常
     *
     * @param exception the exception
     * @return boolean
     */
    public final boolean notifyFailure(Exception exception) {
        return mTaskImpl.notifyFailureException(exception);
    }

    /**
     * 获取Task
     *
     * @return task
     */
    public final IHelloTask<TResult> getTask() {
        return mTaskImpl;
    }
}
