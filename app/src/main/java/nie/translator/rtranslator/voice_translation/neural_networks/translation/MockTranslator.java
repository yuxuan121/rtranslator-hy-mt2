package nie.translator.rtranslator.voice_translation.neural_networks.translation;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock 翻译器——模型文件缺失时自动使用，用于开发和演示
 */
public class MockTranslator {
    private static final Map<String, String> KO_TO_ZH = new HashMap<>();
    private static final Map<String, String> ZH_TO_KO = new HashMap<>();
    
    static {
        add("안녕하세요", "你好");
        add("반갑습니다", "很高兴认识你");
        add("오늘 날씨가 좋네요", "今天天气真好");
        add("점심 먹었어요?", "你吃午饭了吗？");
        add("한국어를 배우고 있어요", "我正在学习韩语");
        add("이 책은 정말 재미있어요", "这本书真的很有趣");
        add("내일 서울에 가요", "我明天去首尔");
        add("감사합니다", "谢谢");
        add("사랑해요", "我爱你");
        add("어디예요?", "在哪里？");
        add("얼마예요?", "多少钱？");
        add("죄송합니다", "对不起");
    }
    
    private static void add(String ko, String zh) {
        KO_TO_ZH.put(ko, zh);
        ZH_TO_KO.put(zh, ko);
    }
    
    public void translate(String text, String srcLang, String tgtLang, HyMt2MnnTranslator.TranslateCallback callback) {
        String result;
        if (srcLang.contains("ko") && tgtLang.contains("zh")) {
            result = KO_TO_ZH.getOrDefault(text, "[Mock] " + text + " → 中文");
        } else if (srcLang.contains("zh") && tgtLang.contains("ko")) {
            result = ZH_TO_KO.getOrDefault(text, "[Mock] " + text + " → 한국어");
        } else {
            result = text;
        }
        callback.onResult(result);
    }
}
