package cn.xylink.mting.presenter;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.Map;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.CreateBroadcastInfo;
import cn.xylink.mting.contract.BroadcastCreateContact;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.utils.OkGoUtils;

/**
 * @author wjn
 * @date 2019/11/18
 */
public class BroadcastCreatePresenter extends BasePresenter<BroadcastCreateContact.ICreateBroadcastView> implements BroadcastCreateContact.Presenter {

    @Override
    public void onCreateBroadcast(Map data, File file) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getCreateBroadcastUrl(), data, file,
                new TypeToken<BaseResponse<CreateBroadcastInfo>>() {
                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse<CreateBroadcastInfo> baseResponse = (BaseResponse<CreateBroadcastInfo>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onSuccessCreateBroadcast(baseResponse);
                        } else {
                            mView.onErrorCreateBroadcast(code, baseResponse.message);
                        }

                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onErrorCreateBroadcast(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
