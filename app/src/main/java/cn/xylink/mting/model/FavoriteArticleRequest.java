package cn.xylink.mting.model;

import cn.xylink.mting.base.BaseRequest;

public class FavoriteArticleRequest extends cn.xylink.mting.base.BaseRequest {
    public static class UnfavorRequest extends BaseRequest {
        String articleIds;

        public UnfavorRequest(String articleIds) {
            this.articleIds = articleIds;
        }
        public String getArticleIds() {
            return articleIds;
        }

        public void setArticleIds(String articleIds) {
            this.articleIds = articleIds;
        }
    }
    String articleId;

    public FavoriteArticleRequest(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

}
