package cn.xylink.mting.bean;

import java.io.Serializable;

/**
 * @author wjn
 * @date 2019/11/18
 */
public class CreateBroadcastInfo implements Serializable {

    /**
     * createAt : 1571643943664
     * creater : null
     * updateAt : 1571643943664
     * updater : null
     * broadcastId : 2019102115454366461645391
     * userId : 1
     * name : 1111
     * info : 2222
     * picture : http://download.test.xylink.cn/resource/M00/3D/4B/rBMAY12tYieARMnOAAAzeyKmeJY675.png
     * deleted : 0
     * status : 0
     */

    private long createAt;
    private Object creater;
    private long updateAt;
    private Object updater;
    private String broadcastId;
    private String userId;
    private String name;
    private String info;
    private String picture;
    private int deleted;
    private int status;

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public Object getCreater() {
        return creater;
    }

    public void setCreater(Object creater) {
        this.creater = creater;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public Object getUpdater() {
        return updater;
    }

    public void setUpdater(Object updater) {
        this.updater = updater;
    }

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
