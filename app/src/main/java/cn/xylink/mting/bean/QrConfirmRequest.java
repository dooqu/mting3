package cn.xylink.mting.bean;

import cn.xylink.mting.base.BaseRequest;

/**
 * 扫码确认/取消接口v1
 * -----------------------------------------------------------------
 * 2020/4/26 14:20 : Create QrConfirmRequest.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class QrConfirmRequest extends BaseRequest {
    public String qrcode;
    //cancel取消
    //submit确认
    public String event;
}
