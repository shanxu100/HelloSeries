package org.helloseries.helloplugin

import com.android.build.gradle.AppExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * 功能描述
 *
 * @since 2020-06-15
 */
class HelloTask extends DefaultTask {

    AppExtension android;
    HelloPluginExtension ext


    /**
     * 构造函数，可以做初始化
     */
    HelloTask() {
        description = 'Hello World Plugin'
        group = "hello plugin group"
        //强制没有缓存
        outputs.upToDateWhen { false }
        android = project.extensions.android
        ext = project.HelloPluginExt
    }

    @TaskAction
    run() {
        // 使用注解定义task需要完成的动作

        android.applicationVariants.all { variant ->

            project.logger.error("###为每一个flavor服务###")
            project.logger.error("buildType=" + variant.buildType.name);
            project.logger.error("applicationId=" + variant.variantData.variantConfiguration.applicationId);
            variant.outputs.each { output ->
                project.logger.error("variant.output=" + output.name)
            }
            project.logger.error("###")
        }

    }

}
