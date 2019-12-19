package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * -----------------------------------------------------------------
 * 2019/12/19 14:43 : Create LinkCheckRequest.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class LinkCheckRequest extends BaseRequest {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
