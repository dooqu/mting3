package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.BroadcastItemAddInfo;
import cn.xylink.mting.bean.BroadcastItemAddRequest;
import cn.xylink.mting.contract.BroadcastItemAddContact;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.utils.OkGoUtils;

/**
 * @author wjn
 * @date 2019/11/26
 */
public class BroadcastItemAddPresenter extends BasePresenter<BroadcastItemAddContact.IBroadcastItemAddView> implements BroadcastItemAddContact.Presenter {
    @Override
    public void getBroadcastItemAddList(BaseRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getBroadcastListUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponseArray<BroadcastItemAddInfo>>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponseArray<BroadcastItemAddInfo> baseResponse = (BaseResponseArray<BroadcastItemAddInfo>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastItemAddListSuccess(baseResponse.data);
                        } else {
                            mView.onBroadcastItemAddListError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastItemAddListError(code, errorMsg);

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getBroadcastItemAdd(BroadcastItemAddRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.addBroadcastItemUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse<String>>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse<String> baseResponse = (BaseResponse<String>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastItemAddSuccess(baseResponse);
                        } else {
                            mView.onBroadcastItemAddError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastItemAddError(code, errorMsg);

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
