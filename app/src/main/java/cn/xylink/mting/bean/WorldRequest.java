package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * 世界列表
 * -----------------------------------------------------------------
 * 2019/11/6 11:54 : Create WorldRequest.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class WorldRequest extends BaseRequest {
    private String event;
    private Long lastAt;

    public enum EVENT{
        OLD,
        NEW;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Long getLastAt() {
        return lastAt;
    }

    public void setLastAt(Long lastAt) {
        this.lastAt = lastAt;
    }
}
