package cn.xylink.mting.bean;

/**
 * 播单item
 * -----------------------------------------------------------------
 * 2019/11/11 15:55 : Create BroadcastInfo.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastInfo {

    /**
     * itemId : 91882b5cd1004ee5a8d4f2e7357d4df8
     * articleId : 2019101815534266457766287
     * title : 华为汽车局
     * url : https://mp.weixin.qq.com/s/66gUhm5lDPcYZGsEJlnOdg
     * picture :
     * sourceName : “”
     * store : 0
     * progress : 0
     * shareUrl : http://test.xylink.cn/article/2019101815534266457766287
     * createAt : 1571715987694
     * updateAt : 1571715987694
     */

    private String itemId;
    private String articleId;
    private String title;
    private String url;
    private String picture;
    private String sourceName;
    private int store;
    private Float progress;
    private String shareUrl;
    private Long createAt;
    private Long updateAt;
    private Long lastAt;
    private String describe;
    private int positin;
    private boolean isChecked= false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getPositin() {
        return positin;
    }

    public void setPositin(int positin) {
        this.positin = positin;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
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

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Long getLastAt() {
        return lastAt;
    }

    public void setLastAt(Long lastAt) {
        this.lastAt = lastAt;
    }
}
