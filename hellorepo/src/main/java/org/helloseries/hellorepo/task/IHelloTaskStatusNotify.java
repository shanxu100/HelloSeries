
package org.helloseries.hellorepo.task;

/**
 * 功能描述 Hello task status notify.
 * 改变当前Task的状态接口 notify the status complete/success/failure
 * 通知task执行状态的接口
 */
interface IHelloTaskStatusNotify<TResult> {

    /**
     * notify the status
     *
     * @param task which the status will be notified.
     */
    void notifyStatus(IHelloTask<TResult> task);
}
