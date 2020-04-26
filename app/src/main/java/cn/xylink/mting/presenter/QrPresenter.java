package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.QrConfirmRequest;
import cn.xylink.mting.bean.QrInfo;
import cn.xylink.mting.contract.QrContract;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2020/4/26 14:27 : Create QrPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class QrPresenter extends BasePresenter<QrContract.IQrView> implements QrContract.Presenter {
    @Override
    public void qr(String url, BaseRequest request) {
        OkGoUtils.getInstance().postData(mView, url, new Gson().toJson(request),
                new TypeToken<BaseResponse<QrInfo>>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse<QrInfo> baseResponse = (BaseResponse<QrInfo>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
//                            mView.onQrSuccess(baseResponse.data);
                            QrConfirmRequest request1 = new QrConfirmRequest();
                            request1.qrcode = baseResponse.data.getQrcode();
                            request1.event = "submit";
                            request1.doSign();
                            qrConfirm(request1);
                        } else {
                            mView.onQrError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onQrError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void qrConfirm(QrConfirmRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getQrConfirmUrl(), new Gson().toJson(request),
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
                            mView.onQrSuccess(null);
                        } else {
                            mView.onQrError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onQrError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
