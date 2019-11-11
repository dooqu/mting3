package cn.xylink.mting.bean;

/**
 *享听列表实体
 *
 * -----------------------------------------------------------------
 * 2019/11/5 15:34 : Create TingInfo.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class TingInfo {

    /**
     * broadcastId : 2019102118414971152446751
     * name : 1111
     * info : 2222
     * picture : http://download.test.xylink.cn/resource/M00/3D/4B/rBMAY12ti22ABZ7rAAAzeyKmeJY936.png
     * deleted : 0
     * createAt : 1571654509986
     * updateAt : 1571654509986
     * top : 0
     * newMsg : 0
     * message : null
     */

    private String broadcastId;
    private String name;
    private String info;
    private String picture;
    private int deleted;
    private long createAt;
    private long updateAt;
    private int top;
    private int newMsg;
    private String message;

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getNewMsg() {
        return newMsg;
    }

    public void setNewMsg(int newMsg) {
        this.newMsg = newMsg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
