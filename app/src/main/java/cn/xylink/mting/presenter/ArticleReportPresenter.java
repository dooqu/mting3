package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleReportRequest;
import cn.xylink.mting.contract.ArticleReportContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * @author wjn
 * @date 2019/12/10
 */
public class ArticleReportPresenter extends BasePresenter<ArticleReportContact.IDelStoreView> implements ArticleReportContact.Presenter {
    @Override
    public void getArticleReport(ArticleReportRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getReportSaveUrl(), new Gson().toJson(request),
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
                            mView.onArticleReportSuccess(baseResponse);
                        } else {
                            mView.onArticleReportError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onArticleReportError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
