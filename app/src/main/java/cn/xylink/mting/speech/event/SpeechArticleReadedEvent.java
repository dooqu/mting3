package cn.xylink.mting.speech.event;

import cn.xylink.mting.bean.Article;

public class SpeechArticleReadedEvent extends SpeechEvent {
    public SpeechArticleReadedEvent(Article article) {
        super(article);
    }
}
