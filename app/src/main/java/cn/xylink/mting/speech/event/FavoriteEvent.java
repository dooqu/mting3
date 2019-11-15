package cn.xylink.mting.speech.event;

import cn.xylink.mting.bean.Article;

public class FavoriteEvent extends SpeechEvent {
    public FavoriteEvent(Article article) {
        super(article);
    }
}
