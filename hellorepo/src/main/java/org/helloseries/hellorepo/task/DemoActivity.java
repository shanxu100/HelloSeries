
package org.helloseries.hellorepo.task;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.helloseries.hellorepo.R;

public class DemoActivity extends AppCompatActivity {

    private static final String TAG = "DemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

    }

    public void testIntegerTask(View view) {
        // part A: 创建task实例并传递给part B
        HelloTaskHolder<Integer> taskHolder = new HelloTaskHolder<>();
        IHelloTask<Integer> helloTask = taskHolder.getTask();
        taskHolder.notifyResult(123);
        Log.e(TAG, "task notifyResult");

        // part B: 为task添加listener，获取task的状态
        helloTask.addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                Log.e(TAG, "task result = " + integer);
            }
        });

        helloTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "task onFailure = " + exception);
            }
        });

        helloTask.addOnCompleteListener(new OnCompleteListener<Integer>() {
            @Override
            public void onComplete(IHelloTask<Integer> task) {
                if (!task.isComplete()) {
                    Log.e(TAG, "task is not complete.");

                }
                if (task.isSuccessful()) {
                    Log.e(TAG, "task is completed. result = " + task.getResult());
                } else {
                    Log.e(TAG, "task is completed. exception = " + task.getException());
                }
            }
        });
    }

    public void testIntegerTask2(View view) {
        // part A: 创建task实例并传递给part B

        IHelloTask<Integer> helloTask = HelloTasks.createHelloTask(new RuntimeException("guan exception"));
        Log.e(TAG, "task notifyException");

        // part B: 为task添加listener，获取task的状态
        helloTask.addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                Log.e(TAG, "task result = " + integer);
            }
        });

        helloTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "task onFailure = " + exception);
            }
        });

        helloTask.addOnCompleteListener(new OnCompleteListener<Integer>() {
            @Override
            public void onComplete(IHelloTask<Integer> task) {
                if (!task.isComplete()) {
                    Log.e(TAG, "task is not complete.");

                }
                if (task.isSuccessful()) {
                    Log.e(TAG, "task is completed. result = " + task.getResult());
                } else {
                    Log.e(TAG, "task is completed. exception = " + task.getException());
                }
            }
        });
    }

}