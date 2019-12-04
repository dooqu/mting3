package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.BroadcastListRequest;
import cn.xylink.mting.contract.BroadcastListContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2019/11/11 16:02 : Create BroadcastListPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastListPresenter extends BasePresenter<BroadcastListContact.IBroadcastListView> implements BroadcastListContact.Presenter {
    @Override
    public void getBroadcastList(BroadcastListRequest request, boolean isLoadMore) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getBroadcastlListUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponseArray<BroadcastInfo>>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponseArray<BroadcastInfo> baseResponse = (BaseResponseArray<BroadcastInfo>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastListSuccess(baseResponse, isLoadMore);
                        } else {
                            mView.onBroadcastListError(code, baseResponse.message, isLoadMore);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastListError(code, errorMsg, isLoadMore);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
