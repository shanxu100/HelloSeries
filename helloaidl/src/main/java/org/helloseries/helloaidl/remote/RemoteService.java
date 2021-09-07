
package org.helloseries.helloaidl.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.helloseries.helloaidl.IBookManager;
import org.helloseries.helloaidl.model.Book;

import java.util.ArrayList;
import java.util.List;

public class RemoteService extends Service {
    private static final String TAG = "RemoteService";

    private List<Book> books = new ArrayList<>();

    public RemoteService() {
        Log.e(TAG, "on constructor");
        books.add(new Book("initBookName", "initAuth", 1));
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");

        return new IBookManager.Stub() {
            @Override
            public List<Book> getBooks() throws RemoteException {
                return books;
            }

            @Override
            public void addBook(Book book) throws RemoteException {
                books.add(book);
            }
        };

    }
}