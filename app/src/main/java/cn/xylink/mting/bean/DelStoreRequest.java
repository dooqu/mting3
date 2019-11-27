package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/26 14:26 : Create DelStoreRequest.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class DelStoreRequest extends BaseRequest {
    private String articleIds;

    public String getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(String articleIds) {
        this.articleIds = articleIds;
    }
}
