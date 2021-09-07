package org.helloseries.hellojvm;

import android.content.Context;
import android.util.Log;

/**
 * C++ 反射调用Java的demo方法
 *
 * @since 2021-09-06
 */
public class TestJavaManager {

    private static final String TAG="TestJavaManager";

    public TestJavaManager(){
        Log.e(TAG,"create TestJavaManager instance");
    }

    public void testFun1(Context mContext){

    }

    public static boolean testStaticFun2(){

        return false;
    }

}
