package cn.xylink.mting.speech.event;

import cn.xylink.mting.bean.Article;

public class SpeechEvent
{
    private Article article;

    private SpeechEvent next;

    public Article getArticle()
    {
        return this.article;
    }

    public void setArticle(Article article)
    {
        this.article = article;
    }

    public SpeechEvent()
    {}

    public SpeechEvent(Article article)
    {
        this.article = article;
    }
}
