package cn.xylink.mting.bean;

/**
 * 扫码授权认证接口v1
 * -----------------------------------------------------------------
 * 2020/4/26 14:18 : Create QrInfo.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class QrInfo {
    private String qrcode;
    private String status;

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
