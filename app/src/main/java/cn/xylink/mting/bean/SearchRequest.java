package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * -----------------------------------------------------------------
 * 2019/12/2 11:39 : Create SearchRequest.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class SearchRequest extends BaseRequest {
    private String query;
    private int page;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
