
package org.helloseries.hellorepo.task;

/**
 * 功能描述 On feature complete listener.
 * 描 述: 当与应用市场第一次通信，Task完成的回调
 * Listener called when a HelloTask completes.
 */
public interface OnCompleteListener<TResult> {

    /**
     * Called when the HelloTask completes.
     *
     * @param task :the completed HelloTask. Never null
     */
    void onComplete(final IHelloTask<TResult> task);
}
