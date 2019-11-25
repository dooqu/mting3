package cn.xylink.mting.bean;

import java.io.Serializable;

/**
 * @author wjn
 * @date 2019/11/22
 */
public class ArticleCreateInputInfo implements Serializable {

    /**
     * articleId : 2019101815594237178096376
     * title : 我是标题
     * url : null
     * picture : null
     * content : 我是正文
     * userId : 1
     * sourceName : null
     * store : 0
     * createAt : 1571385582372
     * updateAt : 1571385582372
     * progress : 0
     * shareUrl : http://test.xylink.cn/article/2019101815594237178096376
     * inType : 1
     * checked : 1
     */

    private String articleId;
    private String title;
    private Object url;
    private Object picture;
    private String content;
    private String userId;
    private Object sourceName;
    private int store;
    private long createAt;
    private long updateAt;
    private int progress;
    private String shareUrl;
    private int inType;
    private int checked;

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

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getSourceName() {
        return sourceName;
    }

    public void setSourceName(Object sourceName) {
        this.sourceName = sourceName;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getInType() {
        return inType;
    }

    public void setInType(int inType) {
        this.inType = inType;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
