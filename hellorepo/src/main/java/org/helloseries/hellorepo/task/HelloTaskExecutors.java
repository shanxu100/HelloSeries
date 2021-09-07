
package org.helloseries.hellorepo.task;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * 功能描述 Hello task executors.
 * 使用Task标准的Excutor实例
 * 包含两个Executor，分别是主线程和子线程
 */
public class HelloTaskExecutors {

    /**
     * an Executor that uses the main application thread.
     * 主线程执行
     */
    public static final Executor MAIN_THREAD = new MainThreadExecutor();

    /**
     * used for the java
     * 实例化一个Excutor
     */
    static final Executor EXECUTOR = new Executor() {
        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }
    };

    /**
     * 消息扔到主线程执行
     *
     * @since 2019 /09/25
     */
    static final class MainThreadExecutor implements Executor {

        // 主线程
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public final void execute(Runnable runnable) {
            handler.post(runnable);
        }
    }
}
