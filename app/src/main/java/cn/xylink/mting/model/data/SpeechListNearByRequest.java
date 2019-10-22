package cn.xylink.mting.model.data;

import cn.xylink.mting.model.SpeechListRequest;

public class SpeechListNearByRequest extends SpeechListRequest {
    private String event;
    private long createAt;

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}

