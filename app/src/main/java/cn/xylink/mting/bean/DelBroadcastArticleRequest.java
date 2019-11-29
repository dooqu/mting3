package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/28 11:31 : Create DelBroadcastArticleRequest.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class DelBroadcastArticleRequest extends BaseRequest {
    private String broadcastId;
    private String articleIds;

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(String articleIds) {
        this.articleIds = articleIds;
    }
}
