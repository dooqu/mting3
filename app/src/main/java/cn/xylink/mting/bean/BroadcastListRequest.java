package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * 播单列表
 * -----------------------------------------------------------------
 * 2019/11/11 15:50 : Create BroadcastListRequest.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastListRequest extends BaseRequest {
    /**
     * 待读传-1，已读历史传-2，收藏传-3，我创建的传-4。
     */
    private String broadcastId;

    /**
     * 事件为old时：
     * lastAt必须有值，加载lastAt时间之前的数据
     * 事件为new时：
     * lastAt必须有值，加载lastAt时间之后的数据
     */
    private String event;
    /**
     * 注：首次加载时，正序加载，lastAt不用传，event也不用传，reverse也可不传
     */
    private Long lastAt;
    /**
     * 是否倒序，默认false(正序)，true(倒序)
     */
    private Boolean reverse;

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

    public Long getLastAt() {
        return lastAt;
    }

    public void setLastAt(Long lastAt) {
        this.lastAt = lastAt;
    }

    public Boolean getReverse() {
        return reverse;
    }

    public void setReverse(Boolean reverse) {
        this.reverse = reverse;
    }
}
