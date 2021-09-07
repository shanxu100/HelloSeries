
package org.helloseries.hellorepo.async;

import android.os.Looper;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述
 *
 * @since 2021-03-17
 */
public abstract class AsyncExec {
    private static final String TAG = "AsyncExec";

    public enum ThreadType {
        IO,
        NETWORK,
        CALCULATION,
        SEQUENCE,
        NOTIFY
    }

    private static final int CORE_THREAD_NUMBER = 3;

    private static final int DOWNLOAD_THREAD_NUMBER = 1;

    private static final int CONCURRENT_THREAD_NUMBER = 10;

    private static final int DOWNLOAD_QUEUE_CAPACITY = 10;

    private static final int THREAD_NUMBER = 3;

    private static final int KEEP_ALIVE_TIME = 60;

    private static Map<ThreadType, ExecutorService> executorMap;

    static {
        init();
    }

    static <V> Future<V> submit(Callable<V> callable, ThreadType type) {
        ExecutorService service = executorMap.get(type);
        return service.submit(callable);
    }

    public static void submitIO(Runnable task) {
        submit(task, ThreadType.IO, false);
    }

    public static void submitNet(Runnable task) {
        submit(task, ThreadType.NETWORK, false);
    }

    public static void submitCalc(Runnable task) {
        submit(task, ThreadType.CALCULATION, false);
    }

    public static void submitSeq(Runnable task) {
        submit(task, ThreadType.SEQUENCE, false);
    }

    public static void submitNotify(Runnable task) {
        submit(task, ThreadType.NOTIFY, false);
    }

    public static void submit(Runnable task, ThreadType type, boolean runInSameBackgroundThread) {
        if (task == null) {
            return;
        }

        if (runInSameBackgroundThread && !isMainThread()) {
            TaskWrapper taskWrapper = new TaskWrapper(task);
            taskWrapper.run();
        } else {
            ExecutorService service = getExecutor(type);
            if (null != service) {
                service.execute(new TaskWrapper(task));
            } else {
                Log.w(TAG, "no executor for type: %s " + type);
            }
        }
    }

    // ==============================================
    //
    // ==============================================

    private synchronized static void init() {
        if (executorMap == null) {
            executorMap = new ConcurrentHashMap<>();

            ThreadPoolExecutor ioExecutor = new ThreadPoolExecutor(CORE_THREAD_NUMBER, THREAD_NUMBER, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new AsyncThreadFactory("IO"));
            ioExecutor.allowCoreThreadTimeOut(true);

            ThreadPoolExecutor networkExecutor = new ThreadPoolExecutor(CORE_THREAD_NUMBER, THREAD_NUMBER,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new AsyncThreadFactory("Net"));
            networkExecutor.allowCoreThreadTimeOut(true);

            ThreadPoolExecutor calExecutor = new ThreadPoolExecutor(CORE_THREAD_NUMBER, THREAD_NUMBER, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new AsyncThreadFactory("Cal"));
            calExecutor.allowCoreThreadTimeOut(true);

            ThreadPoolExecutor seqExecutor = new ThreadPoolExecutor(0, 1, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new AsyncThreadFactory("Seq"));
            seqExecutor.allowCoreThreadTimeOut(true);

            ThreadPoolExecutor notifyExecutor =
                new ThreadPoolExecutor(CORE_THREAD_NUMBER, THREAD_NUMBER, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(), new AsyncThreadFactory("Notify"));

            executorMap.put(ThreadType.IO, ioExecutor);
            executorMap.put(ThreadType.NETWORK, networkExecutor);
            executorMap.put(ThreadType.CALCULATION, calExecutor);
            executorMap.put(ThreadType.SEQUENCE, seqExecutor);
            executorMap.put(ThreadType.NOTIFY, notifyExecutor);

        }
    }

    private static boolean isMainThread() {
        Looper mainLooper = Looper.getMainLooper();
        return mainLooper != null && Thread.currentThread() == mainLooper.getThread();
    }

    private static ExecutorService getExecutor(ThreadType type) {
        return executorMap.get(type);
    }

}
