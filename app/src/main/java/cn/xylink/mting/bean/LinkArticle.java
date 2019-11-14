package cn.xylink.mting.bean;


public class LinkArticle
{

    /**
     * existUnread : 1
     * unreadId : 016b8d6b899244e88f615f5b4f6eff3b
     * articleId : 2019101815534266457766287
     * updateAt : 1571385222666
     * share : 1
     * shareUrl : http://test.xylink.cn/article/2019101815534266457766287
     * sourceName :
     * describe : “华为的基因是一家愿意也善于走远路的公司，用最好的技术，解决最难的问题...
     * content : “华为的基因是一家愿意也善于走远路的公司，用最好的技术，解决最难的问题...
     * title : 华为汽车局
     * url : https://mp.weixin.qq.com/s/66gUhm5lDPcYZGsEJlnOdg
     * picture :
     */

    private int existUnread;
    private String unreadId;
    private String articleId;
    private long updateAt;
    private int share;
    private String shareUrl;
    private String sourceName;
    private String describe;
    private String content;
    private String title;
    private String url;
    private String picture;

    public int getExistUnread() {
        return existUnread;
    }

    public void setExistUnread(int existUnread) {
        this.existUnread = existUnread;
    }

    public String getUnreadId() {
        return unreadId;
    }

    public void setUnreadId(String unreadId) {
        this.unreadId = unreadId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
