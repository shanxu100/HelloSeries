
package org.helloseries.hellorepo.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.net.Inet4Address;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述
 *
 * @since 2021-08-12
 */
public class NetworkManager {

    private static final String TAG = "NetworkManager";

    @SuppressWarnings("MissingPermission")
    public static void checkNetwork(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        Network[] networks = connMgr.getAllNetworks();
        Log.e(TAG, "networks size is " + networks.length);
        for (Network network : networks) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
                Log.d(TAG, "detail 1:" + networkInfo.getDetailedState());
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
                Log.d(TAG, "detail 2:" + networkInfo.getDetailedState());
            }
        }
        Log.d(TAG, "Wifi connected: " + isWifiConn);
        Log.d(TAG, "Mobile connected: " + isMobileConn);
    }

    @SuppressWarnings("MissingPermission")
    public static void checkActiveNetwork(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network network = connMgr.getActiveNetwork();
            Log.e(TAG, "networkInfo netId is " + network.toString() + " is " + network.getByName("rmnet0"));
            boolean result = (network != null);
            Log.e(TAG, "result is " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("MissingPermission")
    public static String getCellularIpWithBlocking(Context context) {
        Log.e(TAG, "getCellularIpWithBlocking1");
        final String[] res = {""};
        ConnectivityManager mConnectivityManager =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return res[0];
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            ConnectivityManager.NetworkCallback callback = null;
            final CountDownLatch latch = new CountDownLatch(1);
            try {
                callback = new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull final Network network) {
                        super.onAvailable(network);
                        Log.e(TAG, "getCellularIpWithBlocking..." + network.toString());

                        try {
                            Socket socket = new Socket("127.0.0.1", 15009);
                            // SocketAddress address = new SocketAddress();
                            // socket.bind("127.0.0.1",1314);
                            network.bindSocket(socket);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        LinkProperties ipInfo = connectivityManager.getLinkProperties(network);
                        List<LinkAddress> linkAddresses = ipInfo.getLinkAddresses();
                        for (LinkAddress linkAddress : linkAddresses) {
                            if (linkAddress.getAddress() instanceof Inet4Address) {
                                res[0] = linkAddress.getAddress().getHostAddress();
                                break;
                            }
                        }
                        Log.e(TAG, "Ip is " + res[0]);

                        latch.countDown();
                    }
                };
                connectivityManager.requestNetwork(buildNetworkRequest(), callback);
                boolean isSuccess = latch.await(1000, TimeUnit.MILLISECONDS);
            } catch (Throwable throwable) {
                Log.v(TAG, "get mobile type error", throwable);
            } finally {

            }

            ConnectivityManager.NetworkCallback finalCallback = callback;
            Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "finally unRegisterNetworkCallback");
                    unRegisterNetworkCallback(connectivityManager, finalCallback);
                }
            }, 5000, TimeUnit.MILLISECONDS);

        }
        return res[0];
    }

    private void unRegister() {

    }

    /**
     * 构建请求：通过蜂窝网络进行通信
     *
     * @return NetworkRequest
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static NetworkRequest buildNetworkRequest() {
        return new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build();
    }

    /**
     * 取消注册：
     *
     * @param connectivityManager
     * @param callback
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void unRegisterNetworkCallback(ConnectivityManager connectivityManager,
        ConnectivityManager.NetworkCallback callback) {
        if (connectivityManager != null && callback != null) {
            connectivityManager.unregisterNetworkCallback(callback);
        }
    }

}
