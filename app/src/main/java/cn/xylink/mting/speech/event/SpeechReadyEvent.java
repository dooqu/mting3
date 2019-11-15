package cn.xylink.mting.speech.event;

import cn.xylink.mting.bean.Article;

/*
正文就绪
 */
public class SpeechReadyEvent extends SpeechEvent {
    public SpeechReadyEvent(Article article) {
        super(article);
    }
}
