package cn.xylink.mting.bean;

import java.io.Serializable;

/**
 * -----------------------------------------------------------------
 * 2019/11/19 17:23 : Create BroadcastDetailInfo.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastDetailInfo implements Serializable {

    /**
     * broadcastId : 2019102115442314581536879
     * createUserId : 1
     * createName : 轩辕联
     * name : 22
     * info : 播单信息
     * picture : http://download.test.xylink.cn/resource/M00/3D/4B/rBMAY12tYgOAG994AAAzeyKmeJY526.png
     * deleted : 0
     * share : 0
     * createAt : 1571643863145
     * updateAt : 1571643907723
     * subscribeTotal : 0
     * top : 0
     * subscribe : 0
     */

    private String broadcastId;
    private String createUserId;
    private String createName;
    private String name;
    private String info;
    private String picture;
    private int deleted;
    private int share;
    private long createAt;
    private long updateAt;
    private int subscribeTotal;
    private int top;
    private int subscribe;
    private String shareUrl;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
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

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
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

    public int getSubscribeTotal() {
        return subscribeTotal;
    }

    public void setSubscribeTotal(int subscribeTotal) {
        this.subscribeTotal = subscribeTotal;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }
}
