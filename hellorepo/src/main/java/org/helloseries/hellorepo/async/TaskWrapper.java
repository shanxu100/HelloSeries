
package org.helloseries.hellorepo.async;

/**
 * 功能描述
 *
 * @since 2021-03-17
 */
public class TaskWrapper implements Runnable {
    private static final String TAG = "TaskWrapper";

    private Runnable runnable;

    public TaskWrapper(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        if (runnable != null) {
            try {
                runnable.run();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
