
package org.helloseries.hellorepo.task;

/**
 * 功能描述 On feature success listener.
 * 描 述: 当HelloTask执行成功的回调
 * Listener called when a HelloTask completes successfully.
 */
public interface OnSuccessListener<TResult> {

    /**
     * Called when the HelloTask completes successfully.
     *
     * @param result : the result of the HelloTask
     */
    void onSuccess(final TResult result);
}
