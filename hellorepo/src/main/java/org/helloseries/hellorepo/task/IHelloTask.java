
package org.helloseries.hellorepo.task;

import java.util.concurrent.Executor;

/**
 * 功能描述 Hello task.
 * 功能描述 与应用市场通信，请求安装或者卸载Split Hello的结果
 * 定义Task的执行方法
 */
public interface IHelloTask<TResult> {

    /**
     * Returns true if the HelloTask is complete; false otherwise.
     * 是否HelloTask已经执行结束，结束的结果可能成功也可能失败
     *
     * @return 是否结束的标记 boolean
     */
    boolean isComplete();

    /**
     * Returns true if the HelloTask has completed successfully; false otherwise.
     * 是否HelloTask已经执行成功
     *
     * @return 是否成功 boolean
     */
    boolean isSuccessful();

    /**
     * Gets the result of the HelloTask, if it has already completed.
     * 获取执行的结果
     *
     * @return 返回获取的结果 result
     */
    TResult getResult();

    /**
     * 返回Hello Task执行的结果，前提是当前Task已经完成
     *
     * @param <X> 抛出的异常
     * @param exceptionType 异常的类型
     * @return 返回的异常结果 result
     * @throws X 异常
     * @throws Throwable the throwable
     */
    <X extends Throwable> TResult getResult(final Class<X> exceptionType) throws X, Throwable;

    /**
     * Returns the exception that caused the HelloTask to fail.
     * 获取HelloTask的异常，如果当前的HelloTask失败
     *
     * @return 返回具体的异常 exception
     */
    Exception getException();

    /**
     * Adds a listener that is called if the HelloTask completes successfully.
     * 当HelloTask完成的时候，监听是否成功
     *
     * @param successListener the success listener
     * @return 返回完成HelloTask feature task
     */
    IHelloTask<TResult> addOnSuccessListener(final OnSuccessListener<? super TResult> successListener);

    /**
     * Adds a listener that is called if the HelloTask completes successfully.
     * 当HelloTask完成的时候，监听是否成功，回调异步执行
     *
     * @param executor 异步执行
     * @param successListener the success listener
     * @return 返回完成HelloTask feature task
     */
    IHelloTask<TResult> addOnSuccessListener(final Executor executor,
        final OnSuccessListener<? super TResult> successListener);

    /**
     * Adds a listener that is called if the HelloTask fails.
     * 当HelloTask完成的时候，监听是否失败
     *
     * @param failureListener the failure listener
     * @return 返回完成的HelloTask feature task
     */
    IHelloTask<TResult> addOnFailureListener(final OnFailureListener failureListener);

    /**
     * Adds a listener that is called if the HelloTask fails.
     * 当HelloTask完成的时候，监听是否失败，回调异步接口
     *
     * @param executor 异步执行
     * @param failureListener the failure listener
     * @return 完成的HelloTask feature task
     */
    IHelloTask<TResult> addOnFailureListener(final Executor executor, final OnFailureListener failureListener);

    /**
     * Adds a listener that is called when the HelloTask completes.
     * 当HelloTask完成的时候，监听是否完成
     *
     * @param listener 监听
     * @return 已经完成的task feature task
     */
    IHelloTask<TResult> addOnCompleteListener(final OnCompleteListener<TResult> listener);

    /**
     * Adds a listener that is called when the HelloTask completes.
     * 当HelloTask完成的时候，监听是否完成，回调异步接口
     *
     * @param executor 异步执行
     * @param listener 监听
     * @return 已经完成task feature task
     */
    IHelloTask<TResult> addOnCompleteListener(final Executor executor, final OnCompleteListener<TResult> listener);
}
