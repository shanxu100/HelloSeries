
package org.helloseries.hellorepo.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

/**
 * 功能描述
 *
 * @since 2021-03-31
 */
public class LifecycleChecker implements LifecycleObserver {

    private static final String TAG = "LifecycleChecker";

    private static LifecycleChecker checker = null;

    private Callback callback = null;

    public static void start(Callback callback) {
        if (checker != null) {
            Log.i(TAG, "already start...");
            return;
        }
        checker = new LifecycleChecker(callback);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(checker);
        Log.i(TAG, "start...");
    }

    public static void stop() {
        if (checker != null) {
            ProcessLifecycleOwner.get().getLifecycle().removeObserver(checker);
        }
        Log.i(TAG, "stop...");
    }

    /**
     * 前后台回调
     */
    public interface Callback {

        /**
         * app运行在后台
         */
        void onAppBackground();

        /**
         * app运行在前台
         */
        void onAppForeground();

    }

    // ========================================================================
    //
    // ========================================================================

    private LifecycleChecker(Callback callback) {
        this.callback = callback;

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackground() {
        // 应用进入后台
        Log.d(TAG, "onAppBackground...");
        if (callback != null) {
            callback.onAppBackground();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForeground() {
        // 应用进入前台
        Log.d(TAG, "onAppForeground...");
        if (callback != null) {
            callback.onAppForeground();
        }
    }

}
