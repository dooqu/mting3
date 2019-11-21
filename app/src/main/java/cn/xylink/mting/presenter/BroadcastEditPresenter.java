package cn.xylink.mting.presenter;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.Map;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.contract.BroadcastEditContact;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.utils.OkGoUtils;

/**
 * @author wjn
 * @date 2019/11/18
 */
public class BroadcastEditPresenter extends BasePresenter<BroadcastEditContact.IBroadcastEditView> implements BroadcastEditContact.Presenter {

    @Override
    public void onBroadcastEdit(Map data, File file) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getEditBroadcastUrl(), data, file,
                new TypeToken<BaseResponse>() {
                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse baseResponse = (BaseResponse) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onSuccessBroadcastEdit(baseResponse);
                        } else {
                            mView.onErrorBroadcastEdit(code, baseResponse.message);
                        }

                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onErrorBroadcastEdit(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
