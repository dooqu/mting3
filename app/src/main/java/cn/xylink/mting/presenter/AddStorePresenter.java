package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.AddStoreRequest;
import cn.xylink.mting.contract.AddStoreContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2019/11/26 14:32 : Create AddStorePresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class AddStorePresenter extends BasePresenter<AddStoreContact.IAddStoreView> implements AddStoreContact.Presenter {
    @Override
    public void addStore(AddStoreRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getAddStoreUrl(), new Gson().toJson(request),
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
                            mView.onAddStoreSuccess(baseResponse);
                        } else {
                            mView.onAddStoreError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onAddStoreError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
