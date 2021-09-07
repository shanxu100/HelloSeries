
package org.helloseries.hellorepo.exception;

import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * App级别的未捕获异常兜底Handler
 * 使用：
 * AppExceptionHandler handler = new AppExceptionHandler();
 * handler.init();
 *
 * @since 2021-03-30
 */
public class AppExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "ExceptionManager";

    private Thread.UncaughtExceptionHandler defaultHandler = null;

    /**
     * 初始化
     */
    public void init() {
        Log.i(TAG, "init...begin");
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        Log.i(TAG, "init...end");
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

        if (defaultHandler != null) {
            // 将defaultHandler 重新注册为默认handler
            Thread.setDefaultUncaughtExceptionHandler(defaultHandler);
        }

        // 自定义处理方法
        handleException(t, e);

        if (defaultHandler != null) {
            // 重新丢回给 defaultHandler 处理
            defaultHandler.uncaughtException(t, e);
        } else {
            // 否则，杀死进程并退出
            Log.w(TAG, "need kill Process and exit...");
            Process.killProcess(Process.myPid());
            System.exit(10);
        }

    }

    /**
     * 发生未捕获异常时的处理函数
     *
     * @param t 发生异常时的线程
     * @param e exception
     */
    private void handleException(Thread t, Throwable e) {
        Log.i(TAG, "Handle Exception ...begin...current ThreadId = " + Thread.currentThread().getId()
            + "current ThreadName = " + Thread.currentThread().getName());

        Log.e(TAG, "The thread instance where exception happened is " + t.getName() + "(id:  " + t.getId() + ")");

        e.printStackTrace();
        Log.i(TAG, "Handle Exception ...end");

    }

}
