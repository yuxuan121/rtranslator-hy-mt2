package nie.translator.rtranslator.voice_translation.neural_networks.translation;

import android.content.Context;
import android.util.Log;
import java.io.File;

/**
 * HY-MT2 + MNN Chat 翻译适配器
 * 替换 RTranslator 原有的 NLLB/OpusMT 翻译引擎
 * 
 * 使用方式：
 * 1. 将 hy-mt2-small-zh-ko-q4.mnn 放到 app/src/main/assets/models/
 * 2. 将 libMNN.so / libMNNChat.so 放到 app/src/main/jniLibs/arm64-v8a/
 * 3. 引擎自动加载真实模型；若文件缺失，降级为 Mock 模式
 */
public class HyMt2MnnTranslator {
    private static final String TAG = "HyMt2MnnTranslator";
    private static final String MODEL_PATH = "models/hy-mt2-small-zh-ko-q4.mnn";
    
    private long nativeHandle = 0;
    private boolean modelLoaded = false;
    private final Context context;
    private final MockTranslator fallback;
    
    public HyMt2MnnTranslator(Context context) {
        this.context = context;
        this.fallback = new MockTranslator();
    }
    
    public void load() {
        File modelFile = new File(context.getFilesDir().getParent(), "assets/" + MODEL_PATH);
        File libMnn = new File(context.getApplicationInfo().nativeLibDir, "libMNN.so");
        
        if (!modelFile.exists() || modelFile.length() < 100 * 1024 * 1024) {
            Log.w(TAG, "模型文件未就绪，使用 Mock 引擎 (model exists=" + modelFile.exists() + ", size=" + modelFile.length() + ")");
            return;
        }
        
        try {
            System.loadLibrary("MNN");
            System.loadLibrary("MNNChat");
            nativeHandle = nativeLoad(modelFile.getAbsolutePath());
            modelLoaded = nativeHandle != 0;
            Log.i(TAG, "HY-MT2 模型加载成功 handle=" + nativeHandle);
        } catch (Exception e) {
            Log.e(TAG, "模型加载失败，降级为 Mock 引擎", e);
        }
    }
    
    public void translate(String text, String srcLang, String tgtLang, TranslateCallback callback) {
        if (modelLoaded) {
            try {
                String result = nativeTranslate(nativeHandle, text, srcLang, tgtLang);
                callback.onResult(result);
                return;
            } catch (Exception e) {
                Log.e(TAG, "真实翻译失败，降级为 Mock", e);
            }
        }
        fallback.translate(text, srcLang, tgtLang, callback);
    }
    
    public void release() {
        if (modelLoaded && nativeHandle != 0) {
            nativeRelease(nativeHandle);
            nativeHandle = 0;
            modelLoaded = false;
        }
    }
    
    // JNI 方法声明
    private native long nativeLoad(String modelPath);
    private native String nativeTranslate(long handle, String text, String srcLang, String tgtLang);
    private native void nativeRelease(long handle);
    
    public interface TranslateCallback {
        void onResult(String translatedText);
    }
}
