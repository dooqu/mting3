package cn.xylink.mting.speech.event;

public class SpeechStopEvent extends SpeechEvent {
    public static enum StopReason {
        ListIsNull,
        CountDownToZero,
        Error
    }

    public int errorCode;
    public String message;

    public SpeechStopEvent() {
        this.stopReason = StopReason.ListIsNull;
    }

    public SpeechStopEvent(StopReason reason) {
        this.stopReason = reason;
    }

    private StopReason stopReason;

    public StopReason getStopReason() {
        return stopReason;
    }

    public void setStopReason(StopReason stopReason) {
        this.stopReason = stopReason;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
