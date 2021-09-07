
package org.helloseries.hellorepo.exception;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.helloseries.hellorepo.R;

import java.util.concurrent.Executors;

public class ExceptionActivity extends AppCompatActivity {
    private static final String TAG = "ExceptionActivity";

    private TextView tvDoSomethingResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);

        tvDoSomethingResult = findViewById(R.id.tv_do_something_result);
    }

    public void initAppExceptionHandler(View view) {
        // 增加全局的 UncaughtExceptionHandler
        new AppExceptionHandler().init();
    }

    public void initCurrentThreadExceptionHandler(View view) {
        // 为当前线程增加 UncaughtExceptionHandler
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

                Log.i(TAG, "begin...");

                e.printStackTrace();

                Log.i(TAG, "end...");
            }
        });
    }

    public void testThreadGroup(View view) {
        // 测试线程组的方法
        Log.i(TAG, "currentThread's name is " + Thread.currentThread().getThreadGroup().getName());

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "name of newSingleThreadExecutor's thread group is "
                    + Thread.currentThread().getThreadGroup().getName());

                Log.i(TAG, "name of newSingleThreadExecutor's thread group's parent is "
                    + Thread.currentThread().getThreadGroup().getParent().getName());

                // 空指针异常
                // Log.i(TAG, "name of newSingleThreadExecutor's thread group's parent is "
                // + Thread.currentThread().getThreadGroup().getParent().getParent().getName());
            }
        });

    }

    public void triggerCrash(View view) throws Exception {
        // throw new Exception("triggerCrash: test my Exceoption...");
        Log.i(TAG, "triggerCrash......");
        Object o = null;
        o.toString();
    }

    public void doSomething(View view) {

        tvDoSomethingResult.setText("do something result:\n" + System.currentTimeMillis());

    }

}