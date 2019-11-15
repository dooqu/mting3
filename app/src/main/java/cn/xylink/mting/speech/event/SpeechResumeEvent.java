package cn.xylink.mting.speech.event;

import cn.xylink.mting.bean.Article;

public class SpeechResumeEvent extends SpeechEvent {
    public SpeechResumeEvent(Article article) {
        super(article);
    }
}
