# RTranslator + HY-MT2 + MNN 中韩同声传译

基于 [RTranslator](https://github.com/niedev/RTranslator) 框架，集成腾讯 HY-MT2 翻译模型和阿里 MNN 推理引擎。

## 自动编译

推送代码后，GitHub Actions 自动编译 APK。你也可以手动触发：
1. 点击仓库上方 **Actions** 标签
2. 选择 **Build RTranslator APK** → **Run workflow**
3. 等待完成后下载 APK

## 文件说明

| 文件 | 说明 |
|------|------|
| `HyMt2MnnTranslator.java` | HY-MT2 + MNN 翻译引擎适配器 |
| `MockTranslator.java` | 降级引擎（模型未就绪时使用） |
| `.github/workflows/build-apk.yml` | 自动编译配置 |

## 启用真实翻译模型

1. 下载模型文件放入 `app/src/main/assets/models/`
2. 下载 `libMNN.so` / `libMNNChat.so` 放入 `app/src/main/jniLibs/arm64-v8a/`
3. 重新推送，Actions 会自动编译
