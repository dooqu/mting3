package cn.xylink.mting.model;

import cn.xylink.mting.base.BaseRequest;

/**
 * @author wjn
 * @date 2019/11/14
 */
public class VisitorRegisterRequest extends BaseRequest {
    public String random;

    @Override
    public String toString() {
        return "VisitorRegisterRequest{" +
                "random='" + random + '\'' +
                '}';
    }
}
