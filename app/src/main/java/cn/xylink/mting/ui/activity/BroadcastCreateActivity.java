package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.BroadcastCreateInfo;
import cn.xylink.mting.bean.BroadcastCreateRequest;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.contract.BroadcastCreateContact;
import cn.xylink.mting.event.TingRefreshEvent;
import cn.xylink.mting.presenter.BroadcastCreatePresenter;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.ImageUtils;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.widget.EditTextWidthClear;

/**
 * @author wjn
 * @date 2019/11/18
 */
public class BroadcastCreateActivity extends BasePresenterActivity implements BroadcastCreateContact.ICreateBroadcastView, TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_title)
    EditTextWidthClear mTitle;
    @BindView(R.id.et_intro)
    EditTextWidthClear mIntro;
    @BindView(R.id.imv_cover)
    ImageView imgCover;
    @BindView(R.id.tv_change_cover)
    TextView mChangeCover;
    private BroadcastCreatePresenter mBroadcastCreatePresenter;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private File coverFile;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_broadcast_create);
    }

    @Override
    protected void initView() {
        if (ContentManager.getInstance().getVisitor().equals("0")) {//表示是游客登录
            Intent intent = new Intent(new Intent(BroadcastCreateActivity.this, LoginActivity.class));
            intent.putExtra(LoginActivity.LOGIN_ACTIVITY, Const.visitor);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void initData() {
        mBroadcastCreatePresenter = (BroadcastCreatePresenter) createPresenter(BroadcastCreatePresenter.class);
        mBroadcastCreatePresenter.attachView(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("创建播单");
        tvRight.setText("完成");
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mTitle.getText().toString().length() != 0) {
                    tvRight.setTextColor(getResources().getColor(R.color.c488def));
                } else {
                    tvRight.setTextColor(getResources().getColor(R.color.cccccc));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @OnClick({R.id.btn_left, R.id.tv_right, R.id.imv_cover, R.id.tv_change_cover})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_right:
                doCreateBroadcast();
                break;
            case R.id.imv_cover:
            case R.id.tv_change_cover:
                coverFile = null;
                takePhoto.onPickFromGallery();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccessCreateBroadcast(BaseResponse<BroadcastCreateInfo> baseResponse) {
        hideLoading();
        L.v(baseResponse);
        EventBus.getDefault().post(new TingRefreshEvent());
        if (null != baseResponse.data) {
            Intent intent = new Intent(BroadcastCreateActivity.this, BroadcastActivity.class);
            intent.putExtra(BroadcastActivity.EXTRA_BROADCASTID, baseResponse.data.getBroadcastId());
            intent.putExtra(BroadcastActivity.EXTRA_TITLE, baseResponse.data.getName());
            startActivity(intent);
        }
        BroadcastCreateActivity.this.finish();

    }

    @Override
    public void onErrorCreateBroadcast(int code, String errorMsg) {

    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            L.v("resultUri.toString()====" + resultUri.toString());
            String picPath = resultUri.toString().replace("file://", "");
            L.v("picPath=====" + picPath);
            ImageUtils.get().load(imgCover, picPath);
            coverFile = new File(picPath);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void doCreateBroadcast() {
        showLoading();
        if (!TextUtils.isEmpty(mTitle.getText().toString())) {
            Map<String, String> data = new HashMap<>();
            BroadcastCreateRequest request = new BroadcastCreateRequest();
            data.put("token", request.token);
            data.put("timestamp", request.timestamp + "");
            data.put("sign", request.sign);
            data.put("name", mTitle.getText().toString());
            data.put("info", mIntro.getText().toString());
            if (null != coverFile) {
                mBroadcastCreatePresenter.onCreateBroadcast(data, coverFile);
            } else {
                mBroadcastCreatePresenter.onCreateBroadcast(data);
            }
        } else {
            toastShort("创建的播单标题不能为空");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(BroadcastCreateActivity.this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        String path;
        if (coverFile != null) {
            path = coverFile.toString();
        } else {
            path = result.getImage().getOriginalPath();
        }
        if (!TextUtils.isEmpty(path)) {
            cropPic(path);
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    private void cropPic(String result) {
        //第三方裁剪库
        File file = new File(this.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        Uri uri = Uri.fromFile(file);
        Uri uri1 = Uri.parse("file://" + result);
        L.v(uri1);
        UCrop uCrop = UCrop.of(uri1, uri);
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        options.setHideBottomControls(true);
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.white));
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.black_overlay));
        options.setToolbarWidgetColor(ActivityCompat.getColor(this, android.R.color.black));
        options.setFreeStyleCropEnabled(false);
        uCrop.withOptions(options);
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(224, 224);
        uCrop.start(this);
    }
}
