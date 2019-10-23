package cn.xylink.mting.model;

public class ArticleInfoRequest extends cn.xylink.mting.base.BaseRequest {

    protected String articleId;
    protected String broadcastId;

    public void setArticleId(String articleId)
    {
        this.articleId = articleId;
    }

    public String getArticleId()
    {
        return this.articleId;
    }


    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }
}
