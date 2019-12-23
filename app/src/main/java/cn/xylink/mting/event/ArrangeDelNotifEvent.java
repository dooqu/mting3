package cn.xylink.mting.event;

/**
 * -----------------------------------------------------------------
 * 2019/12/23 16:45 : Create ArrangeDelNotifEvent.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class ArrangeDelNotifEvent {

    private String broadcastId;
    private String articleIds;

    public ArrangeDelNotifEvent(String broadcastId, String articleIds) {
        this.broadcastId = broadcastId;
        this.articleIds = articleIds;
    }

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
