package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/19 17:22 : Create BroadcastDetailRequest.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastIdRequest extends BaseRequest {
    private String broadcastId;

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }
}
