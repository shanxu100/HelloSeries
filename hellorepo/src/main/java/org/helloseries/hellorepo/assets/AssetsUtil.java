
package org.helloseries.hellorepo.assets;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 功能描述
 *
 * @since 2020-06-10
 */
public class AssetsUtil {

    private static final String TAG = "AssetsUtil";

    /**
     * 例子：copy Assets目录下的so文件到指定目录
     *
     * @param ctx context
     * @return copy是否成功
     */
    public static boolean exampleCopyFileFromAssets(Context ctx) {

        String srcFileDir = "mydir" + File.separator + "pic";
        String srcFileName = "split-debug.lpk";
        String targetFileDir = ctx.getExternalFilesDir(null).getAbsolutePath();
        String targetFileName = "split-debug.lpk";

        return copyFile(ctx, srcFileDir, srcFileName, targetFileDir, targetFileName);
    }

    /**
     * 例子：获取配置文件
     *
     * @param c 上下文
     * @return Properties
     */
    public static Properties exampleGetProperties(Context c) {
        Properties props = new Properties();
        InputStream in = null;
        try {
            // 方法一：通过activity中的context攻取setting.properties的FileInputStream
            in = c.getAssets().open("appConfig.properties");
            props.load(in);
        } catch (Exception e1) {
            Log.e(TAG, "Get the appConfig failed, please make sure the appConfig file exist!");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "InputStream close error.");
                }
            }
        }
        return props;
    }

    /**
     * 例子：读assets目录下的指定文件
     *
     * @param ctx context
     * @return StringBuilder
     */
    public static StringBuilder exampleReadEmptyZl(Context ctx) {
        String srcFileDir = "";
        String srcFileName = "empty.zl";

        return readIntoBuffer(ctx, srcFileDir, srcFileName);

    }

    // ====================================
    // 以上该类的使用示例
    //
    // 以下为暴露给外部，供其他类调用的方法
    // ====================================

    /**
     * copy操作的具体实现
     *
     * @param ctx context
     * @param srcFileDir 源文件所在的Dir
     * @param srcFileName 源文件的名字
     * @param targetFileDir 目标文件所在的Dir
     * @param targetFileName 目标文件的名字
     * @return copy成功或失败
     */
    public static boolean copyFile(Context ctx, String srcFileDir, String srcFileName, String targetFileDir,
        String targetFileName) {

        try {
            File tDir = new File(targetFileDir);
            if (!tDir.exists() && tDir.mkdirs()) {
                // targetDir 目录不存在，且创建失败
                Log.e(TAG, "targetFileDir does not exist and mkdirs failed");
                return false;
            }
            deleteFileIfExist(targetFileDir, targetFileName);
            FileOutputStream outputStream = new FileOutputStream(new File(targetFileDir, targetFileName));

            // 执行同步read方法，每读一次就回调onReading一次。
            // 读取结束，就调用onFinally
            readFileSync(ctx, srcFileDir, srcFileName, new OnReadFileListener() {
                @Override
                public void onReading(byte[] buffer, int length) throws IOException {
                    outputStream.write(buffer, 0, length);
                    outputStream.flush();
                    outputStream.getFD().sync();
                    // outputStream.getChannel().force();
                }

                @Override
                public void onFinally() {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "outputStream close error", e);
                    }
                }
            });

            Log.i(TAG, "copy " + targetFileName + " file from asset successfully");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Exception: Copy File Error!");
            return false;
        }
    }

    /**
     * 读取assets目录下的文件到内存
     *
     * @param ctx
     * @param srcFileDir
     * @param srcFileName
     */
    public static StringBuilder readIntoBuffer(Context ctx, String srcFileDir, String srcFileName) {
        StringBuilder sb = new StringBuilder(4096);
        readFileSync(ctx, srcFileDir, srcFileName, new OnReadFileListener() {
            @Override
            public void onReading(byte[] buffer, int length) {
                sb.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
            }

            @Override
            public void onFinally() {
                Log.i(TAG, "finish reading file into Buffer ");
            }
        });
        return sb;

    }

    // ====================================
    // 以上为暴露给外部，供其他类调用的方法
    //
    // 以下为内部实现方法，不对外暴露
    // ====================================

    /**
     * 如果文件存在，就删除
     *
     * @param fileDir 目标文件夹
     * @param fileName 目标文件名
     */
    private static void deleteFileIfExist(String fileDir, String fileName) {
        try {
            File tFile = new File(fileDir, fileName);
            if (tFile.exists()) {
                if (tFile.delete()) {
                    Log.i(TAG, "success to delete exist file");
                } else {
                    Log.i(TAG, "failed to delete exist file");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "An error occured, file may not exists");
        }
    }

    /**
     * 执行读操作的核心类：同步读文件，并将读到的文件通过listener回调回去
     * “读”时的异常已经在该方法内部catch住，并且在发生异常时做了关闭流的动作
     *
     * @param ctx context
     * @param srcFileDir 源文件所在的Dir
     * @param srcFileName 源文件的名字
     * @param listener 读文件的监听器
     */
    private static void readFileSync(Context ctx, String srcFileDir, String srcFileName, OnReadFileListener listener) {

        BufferedInputStream bufferedInputStream = null;
        try {
            InputStream inputStream = ctx.getAssets().open(new File(srcFileDir, srcFileName).getPath());
            bufferedInputStream = new BufferedInputStream(inputStream);
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
                listener.onReading(buffer, count);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: Read File Error");
        } catch (Exception e) {
            Log.e(TAG, "Exception: Read File Error");
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "bufferedInputStream close error", e);
                }
            }
            listener.onFinally();
        }
    }

    /**
     * 读取文件操作的监听器
     */
    private interface OnReadFileListener {
        /**
         * 持续读入数据
         *
         * @param buffer 存储读入的字节
         * @param length 读入的字节长度
         */
        void onReading(byte[] buffer, int length) throws IOException;

        /**
         * 读取操作结束 或 发生异常结束
         */
        void onFinally();
    }

    // ======================================================
    //
    // ======================================================

    /**
     * 未完成....
     *
     * @param ctx
     * @param targetFileDir
     * @param targetFileName
     * @return
     */
    private static WriteFileProcess writeFileSync(Context ctx, String targetFileDir, String targetFileName) {

        File tDir = new File(targetFileDir);
        if (!tDir.exists() && tDir.mkdirs()) {
            // targetDir 目录不存在，且创建失败
            Log.e(TAG, "targetFileDir does not exist and mkdirs failed");
            return null;
        }
        // 若目标文件已经存在，则删除旧的目标文件，然后再继续copy
        deleteFileIfExist(targetFileDir, targetFileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(new File(targetFileDir, targetFileName));
            WriteFileProcess process = new WriteFileProcess() {
                @Override
                public void onWrite(byte[] buffer, int length) throws IOException {
                    outputStream.write(buffer, 0, length);
                    outputStream.flush();
                    outputStream.getFD().sync();
                }

                @Override
                public void onFinally() {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "outputStream close error", e);
                    }
                }
            };
            return process;
        } catch (Exception e) {
            Log.e(TAG, "Write File failed");
        }
        return null;

    }

    /**
     * 写文件的
     */
    private static abstract class WriteFileProcess {
        /**
         * 持续写
         *
         * @param buffer
         * @param length
         * @throws IOException
         */
        public abstract void onWrite(byte[] buffer, int length) throws IOException;

        public abstract void onFinally();

    }

}
