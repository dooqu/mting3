package cn.xylink.mting.model.data;


import cn.xylink.mting.base.BaseRequest;

/**
 * @author wjn
 * @date 2019/12/4
 */
public class VisitorSyncDataRequest extends BaseRequest {
    private String vstToken;

    public String getVstToken() {
        return vstToken;
    }

    public void setVstToken(String vstToken) {
        this.vstToken = vstToken;
    }

}
