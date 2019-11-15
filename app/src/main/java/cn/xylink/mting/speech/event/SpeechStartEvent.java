package cn.xylink.mting.speech.event;

import cn.xylink.mting.bean.Article;

/*
开始某个Article
 */
public class SpeechStartEvent extends SpeechEvent {
    public SpeechStartEvent(Article article) {
        this.setArticle(article);
    }
}
