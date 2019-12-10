package cn.xylink.mting.presenter;

import android.os.Build;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.xylink.mting.MTing;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.AddFeedbackRequest;
import cn.xylink.mting.contract.AddFeedbackContact;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.GsonUtil;
import cn.xylink.mting.utils.PackageUtils;
import okhttp3.OkHttpClient;

public class AddFeedbackPresenter extends BasePresenter<AddFeedbackContact.IAddFeedBackView> implements AddFeedbackContact.Presenter {
    public void onFeedBackForm(List<File> files, HttpParams param) {
        param.put("timestamp", System.currentTimeMillis());
        param.put("token", ContentManager.getInstance().getLoginToken());
        String url = RemoteUrl.getFeedbackSaveUrl();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间  基于前面的通道建立完成后，客户端终于可以向服务端发送数据了
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间  服务器发回消息，可是客户端出问题接受不到了
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间  http建立通道的时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        OkGo.getInstance().setOkHttpClient(builder.build());
        OkGo.<String>post(url)
                .tag(this)
                .isMultipart(true)
                .headers("timestamp", String.valueOf(System.currentTimeMillis()))
                .headers("version", PackageUtils.getAppVersionCode(MTing.getInstance()))
                .headers("versionName", PackageUtils.getAppVersionName(MTing.getInstance()))
                .headers("deviceId", PackageUtils.getWifiMac(MTing.getInstance()))
                .headers("deviceName", Build.MODEL)
                .headers("sysVersion", "Android " + Build.VERSION.RELEASE)
                .params(param)
                .addFileParams("files", files)
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        BaseResponse<String> baseResponse = GsonUtil.GsonToBean(body, BaseResponse.class);
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onAddFeedBackSuccess(baseResponse);
                        } else {
                            mView.onBindCheckError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onFinish() {
                        mView.hideLoading();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String string = response.body().string();
                        return string;
                    }
                });
    }

    @Override
    public void onFeedBack(AddFeedbackRequest request) {

    }
}
