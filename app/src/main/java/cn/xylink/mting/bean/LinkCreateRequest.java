package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

public class LinkCreateRequest extends BaseRequest {

    /**1.链接分享,2.链接录入*/
    private int inType;
    private String url;


    public int getInType() {
        return inType;
    }

    public void setInType(int inType) {
        this.inType = inType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
