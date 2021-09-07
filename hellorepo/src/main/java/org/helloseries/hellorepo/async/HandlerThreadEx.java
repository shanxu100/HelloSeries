
package org.helloseries.hellorepo.async;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

/**
 * 扩展的HandlerThread类，
 * 特点：防止创建的子线程中出现未捕获的异常，导致线程终止
 *
 * @since 2021-01-09
 */
public class HandlerThreadEx extends HandlerThread {

    private static final String TAG = "HandlerThreadEx";

    private boolean quit = false;

    public HandlerThreadEx(String name) {
        super(name);
    }

    public HandlerThreadEx(String name, int priority) {
        super(name, priority);
    }

    @Override
    public void run() {
        try {
            super.run();
        } catch (Exception e) {
            Log.e(TAG, "looper crashed super.run()", e);
        }

        while (!quit) {
            try {
                Looper.loop();
            } catch (Exception e) {
                Log.e(TAG, "looper crashed Looper.loop()", e);
            }
        }

    }

    @Override
    public boolean quit() {
        Log.i(TAG, "quit");
        quit = true;
        return super.quit();
    }

    @Override
    public boolean quitSafely() {
        Log.i(TAG, "quitSafely");
        quit = true;
        return super.quitSafely();
    }

    @Override
    public int getThreadId() {
        Log.i(TAG, "getThreadId");
        if (quit) {
            return -1;
        } else {
            return Process.myTid();
        }
    }
}
