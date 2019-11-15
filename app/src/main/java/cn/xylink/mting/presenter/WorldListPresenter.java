package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.WorldInfo;
import cn.xylink.mting.bean.WorldRequest;
import cn.xylink.mting.contract.WorldListContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * 世界列表
 * -----------------------------------------------------------------
 * 2019/11/6 14:05 : Create WorldListPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class WorldListPresenter extends BasePresenter<WorldListContact.IWorldListView> implements WorldListContact.Presenter {
    @Override
    public void getWorldList(WorldRequest request, boolean isLoadMore) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getWordlListUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponseArray<WorldInfo>>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponseArray<WorldInfo> baseResponse = (BaseResponseArray<WorldInfo>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onWorldListSuccess(baseResponse.data, isLoadMore);
                        } else {
                            mView.onWorldListError(code, baseResponse.message, isLoadMore);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onWorldListError(code, errorMsg, isLoadMore);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}

