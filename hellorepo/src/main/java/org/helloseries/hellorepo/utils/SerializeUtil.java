
package org.helloseries.hellorepo.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 序列化工具
 *
 * @since 2020-05-19
 */
public class SerializeUtil {
    private static final String TAG = "SerializeUtil";

    /**
     * 序列化
     *
     * @param serializable 序列化对象
     * @return byte[]
     */
    public static byte[] object2Bytes(Serializable serializable) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(serializable);
            return baos.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param bytes 序列化数据
     * @return Object
     */
    public static Object byte2Object(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
