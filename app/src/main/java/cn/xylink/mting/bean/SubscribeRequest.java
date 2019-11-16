package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/15 17:24 : Create SubscribeRequest.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class SubscribeRequest extends BaseRequest {
    private String broadcastId;
    private String event;
    public enum EVENT{
        SUBSCRIBE,
        CANCEL
    }

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
