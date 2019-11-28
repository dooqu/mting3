package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.bean.BroadcastIdRequest;
import cn.xylink.mting.contract.BroadcastDetailContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2019/11/19 17:26 : Create BroadcastDetailPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastDetailPresenter extends BasePresenter<BroadcastDetailContact.IBroadcastDetailView> implements BroadcastDetailContact.Presenter {
    @Override
    public void getBroadcastDetail(BroadcastIdRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getBroadcastDetailUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse<BroadcastDetailInfo>>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse<BroadcastDetailInfo> baseResponse = (BaseResponse<BroadcastDetailInfo>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastDetailSuccess(baseResponse.data);
                        } else {
                            mView.onBroadcastDetailError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastDetailError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
