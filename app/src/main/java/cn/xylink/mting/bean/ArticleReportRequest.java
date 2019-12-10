package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * @author wjn
 * @date 2019/12/10
 */
public class ArticleReportRequest extends BaseRequest {
    private String articleId;
    private String content;
    private String type;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
