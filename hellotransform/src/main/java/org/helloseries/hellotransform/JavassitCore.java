
package org.helloseries.hellotransform;

import com.android.build.gradle.AppExtension;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import org.gradle.api.Project;

import java.io.File;

/**
 * 功能描述
 *
 * @since 2021-02-05
 */
class JavassitCore {
    private static final ClassPool sClassPool = ClassPool.getDefault();

    /**
     * 插入一段Toast代码
     *
     * @param path
     * @param project
     */
    static void injectToast(String path, Project project) throws Exception {
        // 加入当前路径
        sClassPool.appendClassPath(path);
        // project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
        sClassPool.appendClassPath(
            project.getExtensions().findByType(AppExtension.class).getBootClasspath().get(0).toString());

        // 引入android.os.Bundle包，因为onCreate方法参数有Bundle
        sClassPool.importPackage("android.os.Bundle");

        File dir = new File(path);
        if (dir.isDirectory()) {
            // 遍历文件夹
            for (File file : dir.listFiles()) {
                String filePath = file.getAbsolutePath();
                project.getLogger().info("filePath: " + filePath);

                if ("MainActivity.class".equals(file.getName())) {
                    // 获取Class
                    // 这里的MainActivity就在app模块里
                    CtClass ctClass = sClassPool.getCtClass("org.helloseries.helloplugin.MainActivity");
                    project.getLogger().info("ctClass: " + ctClass);

                    // 解冻
                    if (ctClass.isFrozen()) {
                        ctClass.defrost();
                    }

                    // 获取Method
                    CtMethod ctMethod = ctClass.getDeclaredMethod("'onCreate'");
                    project.getLogger().info("ctMethod:" + ctMethod);

                    String toastStr =
                        "android.widget.Toast.makeText(this,\"我是被插入的Toast代码~!!\",android.widget.Toast.LENGTH_SHORT).show();";

                    // 方法尾插入
                    ctMethod.insertAfter(toastStr);
                    ctClass.writeFile(path);
                    ctClass.detach(); // 释放
                }
            }

        }
    }

}
