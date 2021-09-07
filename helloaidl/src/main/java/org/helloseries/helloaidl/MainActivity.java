
package org.helloseries.helloaidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.helloseries.helloaidl.model.Book;
import org.helloseries.helloaidl.remote.RemoteService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ServiceConnection serviceConnection = null;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void bindRemoteService(View view) {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.e(TAG, "onServiceConnected:" + componentName.getClassName());
                IBookManager bookManager = IBookManager.Stub.asInterface(iBinder);
                try {
                    doBusiness(bookManager);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.e(TAG, "onServiceDisconnected:" + componentName.getClassName());

            }
        };
        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.e(TAG, "start bind service");
    }

    public void unbindRemoteService(View view) {
        if (serviceConnection != null) {
            unbindService(serviceConnection);
            serviceConnection = null;
            Log.e(TAG, "unbind service");

        }
    }

    private void doBusiness(IBookManager bookManager) throws RemoteException {

        if (bookManager == null) {
            Log.e(TAG, "bookManager is null");
            return;
        }

        List<Book> books = bookManager.getBooks();
        Log.i(TAG, "book list is " + books);

        bookManager.addBook(new Book("GBook", "GG", 2));

        List<Book> books2 = bookManager.getBooks();
        Log.i(TAG, "book list2 is " + books2);

    }

}