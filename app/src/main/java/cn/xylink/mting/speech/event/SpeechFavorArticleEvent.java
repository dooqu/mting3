package cn.xylink.mting.speech.event;


import cn.xylink.mting.bean.Article;

@Deprecated
public class SpeechFavorArticleEvent extends SpeechEvent {
    public SpeechFavorArticleEvent(Article article) {
        super(article);
    }
}
