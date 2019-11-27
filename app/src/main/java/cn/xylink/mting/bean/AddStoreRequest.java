package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/26 14:25 : Create AddStoreRequest.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class AddStoreRequest extends BaseRequest {
    private String articleId;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
