package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * @author wjn
 * @date 2019/11/22
 */
public class ArticleCreateInputRequest extends BaseRequest {
    private String title;
    private String content;

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
}
