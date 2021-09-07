
package org.helloseries.helloaidl.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 功能描述
 *
 * @since 2021-02-19
 */
public class Book implements Parcelable {

    private String name;

    private String auth;

    private int price;

    public Book() {
    }

    public Book(String name, String auth, int price) {
        this.name = name;
        this.auth = auth;
        this.price = price;
    }

    protected Book(Parcel in) {
        name = in.readString();
        auth = in.readString();
        price = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(auth);
        dest.writeInt(price);
    }
}
