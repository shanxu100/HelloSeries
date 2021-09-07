
package org.helloseries.hellotransform;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;

import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Transform任务定义
 */
class HelloTransform extends Transform {

    private Project project;

    public HelloTransform(Project project) {
        this.project = project;
    }

    @Override
    public String getName() {
        // 定义该Transform Task的名字
        // 如：最终的名称格式为 transformClassesWithHelloTransformForRelease
        return "helloTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        // 用于指明 Transform 的输入类型，可以作为输入过滤的手段。
        // 这样确保其他类型的文件不会传入
        // 此处代表 javac 编译成的 class 文件，常用
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        // 指定Transform的作用范围
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        // 当前Transform是否支持增量编译
        // 需要注意的是，即使返回了 true ，在某些情况下运行时，效果和false一样。
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation)
        throws TransformException, InterruptedException, IOException {
        // 实现 transform 的核心接口

        // project.getLogger().info("transformInvocation.getInput().size = " + transformInvocation.getInputs().size());

        for (TransformInput input : transformInvocation.getInputs()) {

            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                project.getLogger()
                    .info("directoryInput---getName: " + directoryInput.getName() + "  getContentTypes: "
                        + directoryInput.getContentTypes() + "  getScopes: " + directoryInput.getScopes().toString());

                // 注入代码
                try {
                    JavassitCore.injectToast(directoryInput.getFile().getAbsolutePath(), project);
                } catch (Exception e) {
                    project.getLogger().error("JavassitCore.injectToast error");
                    e.printStackTrace();
                }

                // 获取输出目录
                File dest = transformInvocation.getOutputProvider()
                    .getContentLocation(directoryInput.getName(), directoryInput.getContentTypes(),
                        directoryInput.getScopes(), Format.DIRECTORY);

                project.getLogger().info("directory output dest:" + dest.getAbsolutePath());
                // // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(directoryInput.getFile(), dest);

            }

            // for (JarInput jarInput : input.getJarInputs()) {
            // project.getLogger()
            // .info("jarInput---getName: " + jarInput.getName() + " getContentTypes: "
            // + jarInput.getContentTypes() + " getScopes: " + jarInput.getScopes().toString());
            // }
        }

    }
}
