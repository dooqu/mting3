package cn.xylink.mting.speech.event;

import cn.xylink.mting.speech.SpeechError;

public class SpeechSerieLoaddingEvent extends SpeechEvent {
    private String articleId;
    private String articleTitle;
    private String serieTitle;


    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }


    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }


    public void setSerieTitle(String serieTitle) {
        this.serieTitle = serieTitle;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getSerieTitle() {
        return serieTitle;
    }
}
