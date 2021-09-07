
package org.helloseries.hellorepo.assets;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.helloseries.hellorepo.R;

public class AssetsDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_demo);
        Log.e("AssetsDemoActivity", "onCreate");
        AssetsUtil.exampleCopyFileFromAssets(this);
    }

}