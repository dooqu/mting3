package cn.xylink.mting.model.data;

import cn.xylink.mting.model.SpeechListRequest;

public class SpeechListNearByRequest extends SpeechListRequest {
    private String event;
    private long lastAt;

    public long getLastAt() {
        return lastAt;
    }

    public void setLastAt(long createAt) {
        this.lastAt = createAt;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}

