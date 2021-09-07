
package org.helloseries.hellorepo.task;

/**
 * 功能描述 On feature failure listener.
 * 描 述: 一个HelloTask请求失败的回调，返回一个Exception
 * Listener called when a HelloTask fails with an exception.
 */
public interface OnFailureListener {

    /**
     * Called when the HelloTask fails with an exception.
     *
     * @param exception : the exception that caused the HelloTask to fail. Never null
     */
    void onFailure(final Exception exception);
}
