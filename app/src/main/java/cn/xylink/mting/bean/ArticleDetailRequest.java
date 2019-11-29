package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * @author wjn
 * @date 2019/11/28
 */
public class ArticleDetailRequest extends BaseRequest {
    private String articleId;
    private String broadcastId;

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
