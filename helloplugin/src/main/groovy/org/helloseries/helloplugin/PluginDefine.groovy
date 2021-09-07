package org.helloseries.helloplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 功能描述
 *
 * @since 2020-06-15
 */
class PluginDefine implements Plugin<Project> {
    @Override
    void apply(Project project) {

        // 判断使用该插件的project是否应用了其他插件
        project.plugins.hasPlugin('com.android.application')

        // 为该插件创建名为HelloPluginExt的ext闭包
        // 使用者可以在该ext中指定参数
        project.extensions.create("HelloPluginExt", HelloPluginExtension)

        // 创建名为 printHello 的task
        // 该Task执行的任务在HelloTask中定义
        project.task("printHello", type: HelloTask)

    }
}
