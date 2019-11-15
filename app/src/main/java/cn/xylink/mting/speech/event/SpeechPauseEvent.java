package cn.xylink.mting.speech.event;

import cn.xylink.mting.bean.Article;

public class SpeechPauseEvent extends SpeechEvent {

    public SpeechPauseEvent(Article article) {
        super(article);
    }
}
