package cn.xylink.mting.event;

/**
 * -----------------------------------------------------------------
 * 2019/12/24 10:19 : Create TingChangeMessageEvent.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class TingChangeMessageEvent {

    private String broadcastId;
    private String message;

    public TingChangeMessageEvent(String broadcastId, String message) {
        this.broadcastId = broadcastId;
        this.message = message;
    }

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
