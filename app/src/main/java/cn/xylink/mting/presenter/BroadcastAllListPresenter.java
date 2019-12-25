package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.BroadcastAllListInfo;
import cn.xylink.mting.contract.BroadcastAllListContact;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.utils.OkGoUtils;

/**
 * -----------------------------------------------------------------
 * 2019/12/25 11:11 : Create BroadcastAllListPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastAllListPresenter extends BasePresenter<BroadcastAllListContact.IBroadcastAllListView> implements BroadcastAllListContact.Presenter {

    @Override
    public void getAllList(BaseRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getBroadcastListWithNumUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponseArray<BroadcastAllListInfo>>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponseArray<BroadcastAllListInfo> baseResponse = (BaseResponseArray<BroadcastAllListInfo>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastAllListSuccess(baseResponse.data);
                        } else {
                            mView.onBroadcastAllListError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastAllListError(code, errorMsg);

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}