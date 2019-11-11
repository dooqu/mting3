package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.TingInfo;
import cn.xylink.mting.contract.TingListContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * 享听列表
 * <p>
 * -----------------------------------------------------------------
 * 2019/11/5 15:46 : Create TingListPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class TingListPresenter extends BasePresenter<TingListContact.ITingListView> implements TingListContact.Presenter {
    @Override
    public void getTingList(BaseRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getTingListUrl(), new Gson().toJson(request), new TypeToken<BaseResponseArray<TingInfo>>() {

        }.getType(), new OkGoUtils.ICallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(Object data) {
                BaseResponseArray<TingInfo> baseResponse = (BaseResponseArray<TingInfo>) data;
                int code = baseResponse.code;
                if (code == 200) {
                    mView.onTingListSuccess(baseResponse);
                } else {
                    mView.onTingListError(code, baseResponse.message);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.onTingListError(code, errorMsg);
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
