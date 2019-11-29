package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * @author wjn
 * @date 2019/11/29
 */
public class ArticleEditRequest extends BaseRequest {
    private String articleId;
    private String title;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;
}
