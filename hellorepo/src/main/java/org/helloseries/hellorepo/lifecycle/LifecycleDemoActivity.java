
package org.helloseries.hellorepo.lifecycle;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.helloseries.hellorepo.R;

public class LifecycleDemoActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle_demo);
        LifecycleChecker.start(new LifecycleChecker.Callback() {
            @Override
            public void onAppBackground() {
                Log.e(TAG, "onAppBackground...");

            }

            @Override
            public void onAppForeground() {
                Log.e(TAG, "onAppForeground...");

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LifecycleChecker.stop();
    }

}