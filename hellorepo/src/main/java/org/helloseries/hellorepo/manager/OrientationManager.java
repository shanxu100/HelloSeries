
package org.helloseries.hellorepo.manager;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;

/**
 * 监听设备旋转
 * 开启：OrientationManager.getInstance().start(context);
 * 关闭：OrientationManager.getInstance().stop();
 */
public class OrientationManager {

    private static final String TAG = "OrientationManager";

    private int mOrientation = -1;

    private OrientationEventListener mOrientationListener;

    private WindowManager windowManager;

    public static OrientationManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static final class InstanceHolder {
        private static OrientationManager INSTANCE = new OrientationManager();
    }

    private OrientationManager() {

    }

    /**
     * 开启监听旋转
     *
     * @param context context
     */
    public void start(Context context) {
        Log.i(TAG, "start OrientationManager");

        this.mOrientation = -1;

        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (mOrientationListener == null) {
            mOrientationListener = new OrientationEventListener(context) {

                @Override
                public void onOrientationChanged(int orientation) {
                    if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                        return;
                    }
                    if (checkOrientation(orientation)) {
                        updateOrientation();
                    }

                }
            };
            Log.i(TAG, "first updateOrientation...");
            updateOrientation();
            this.mOrientationListener.enable();
        } else {
            Log.i(TAG, "mOrientationListener is not null");
        }
    }

    /**
     * 停止监听
     */
    public void stop() {
        if (this.mOrientationListener != null) {
            this.mOrientationListener.disable();
            this.mOrientationListener = null;
            Log.i(TAG, "stop OrientationManager");
            return;
        }
        Log.i(TAG, "stop OrientationManager:  mOrientationListener is null!");

    }

    private void updateOrientation() {
        if (windowManager == null) {
            Log.e(TAG, "updateOrientation windowManager is null!");
            return;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        if (defaultDisplay == null) {
            Log.e(TAG, "updateOrientation display is null!");
            return;
        }
        int rotation = defaultDisplay.getRotation();
        if (rotation != mOrientation) {
            Log.i(TAG, "updateOrientation new rotation is " + rotation + ", mOrientation is " + mOrientation);
            switch (rotation) {
                case Surface.ROTATION_0:
                    mOrientation = Surface.ROTATION_0;
                    // 此时发生旋转：0
                    // do something
                    return;
                case Surface.ROTATION_90:
                    mOrientation = Surface.ROTATION_90;
                    // 此时发生旋转：90/270
                    // do something
                    return;
                case Surface.ROTATION_180:
                    mOrientation = Surface.ROTATION_180;
                    // 此时发生旋转：180
                    // do something
                    return;
                case Surface.ROTATION_270:
                    // 此时发生旋转：270/90
                    // do something
                    return;
                default:
                    return;
            }
        }

    }

    public boolean checkOrientation(int orientation) {
        if (orientation != -1) {
            if (orientation > 340 || orientation < 20 || ((orientation > 70 && orientation < 110)
                || ((orientation > 160 && orientation < 200) || (orientation > 250 && orientation < 290)))) {
                return true;
            }
        }
        return false;
    }

}
