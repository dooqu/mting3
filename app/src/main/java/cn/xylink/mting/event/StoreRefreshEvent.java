package cn.xylink.mting.event;

/**
 * -----------------------------------------------------------------
 * 2019/12/6 17:27 : Create StoreRefreshEvent.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class StoreRefreshEvent {
    private int stroe;
    private String articleID;

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public int getStroe() {
        return stroe;
    }

    public void setStroe(int stroe) {
        this.stroe = stroe;
    }
}
