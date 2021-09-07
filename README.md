# HelloSeries：Hello系列组件

## 1、HelloPlugin
自定义gradle plugin开发的HelloWorld类型工程。
旨在介绍如何开发一个最简单的gradle插件

## 2、HelloTask
HelloTask是一个轻量级的task任务执行组件。该组件具有以下特性：
1. task有成功、失败、完成三种监听器。无论task成功或者失败，最终均是完成状态
2. 无论在task执行完成前后（成功或者失败），均可以为task注册listener
3. listener的回调默认在主线程执行

## 3、HelloTransform
使用Transform操作，实现字节码替换的简单工程。
旨在实现一个自定义Transform任务。





