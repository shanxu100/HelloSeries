
package org.helloseries.hellotransform;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * 插件定义
 */
public class HelloTransformPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        project.getLogger().info("hello transform...begin");

        AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
        if (appExtension != null) {
            // 1、实例化自定义Transform任务
            // 2、注册Transform，即添加task
            appExtension.registerTransform(new HelloTransform(project));
        } else {
            project.getLogger().info("hello transform...appExtension is null");

        }

        project.getLogger().info("hello transform...end");

    }
}
