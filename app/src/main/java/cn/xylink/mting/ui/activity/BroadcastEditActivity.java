package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.BroadcastCreateRequest;
import cn.xylink.mting.contract.BroadcastEditContact;
import cn.xylink.mting.presenter.BroadcastEditPresenter;
import cn.xylink.mting.utils.ImageUtils;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.widget.EditTextWidthClear;

/**
 * @author wjn
 * @date 2019/11/21
 */
public class BroadcastEditActivity extends BasePresenterActivity implements BroadcastEditContact.IBroadcastEditView, TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.imv_cover_edit)
    ImageView imgCover;
    @BindView(R.id.et_title_edit)
    EditTextWidthClear mTitle;
    @BindView(R.id.et_intro_edit)
    EditTextWidthClear mIntro;
    private BroadcastEditPresenter mEditPresenter;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private File coverFile = null;
    private String broadcastId = "";
    private String name = "";
    private String intro = "";
    public static String BROADCAST_ID = "BROADCAST_ID";
    public static String BROADCAST_NAME = "BROADCAST_NAME";
    public static String BROADCAST_INTRO = "BROADCAST_INTRO";

    @Override
    protected void preView() {
        setContentView(R.layout.activity_broadcast_edit);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mEditPresenter = (BroadcastEditPresenter) createPresenter(BroadcastEditPresenter.class);
        mEditPresenter.attachView(this);
        broadcastId = getIntent().getStringExtra(BROADCAST_ID);
        name = getIntent().getStringExtra(BROADCAST_NAME);
        intro = getIntent().getStringExtra(BROADCAST_INTRO);
    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("编辑播单");
        tvRight.setText("完成");
        tvRight.setTextColor(getResources().getColor(R.color.c488def));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void onSuccessBroadcastEdit(BaseResponse baseResponse) {
        hideLoading();
        if (baseResponse.code == 200) {
            toastShort(baseResponse.message);
            BroadcastEditActivity.this.finish();
        } else {
            toastShort(baseResponse.message);
        }

    }

    @Override
    public void onErrorBroadcastEdit(int code, String errorMsg) {
        toastShort(errorMsg);
    }

    @Override
    public void takeSuccess(TResult result) {
        String path;
        if (coverFile != null) {
            path = coverFile.toString();
        } else {
            path = result.getImage().getOriginalPath();
        }
        ImageUtils.get().load(imgCover, path);
        coverFile = new File(path);
        L.v("nana", "coverFile--------" + path);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(BroadcastEditActivity.this, type, invokeParam, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            L.v("resultUri.toString()====" + resultUri.toString());
            String picPath = resultUri.toString().replace("file://", "");
            L.v("picPath=====" + picPath);
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

    private void doEditBroadcast() {
        showLoading();
        if (!TextUtils.isEmpty(mTitle.getText().toString())) {
            Map<String, String> data = new HashMap<>();
            BroadcastCreateRequest request = new BroadcastCreateRequest();
            data.put("token", request.token);
            data.put("timestamp", request.timestamp + "");
            data.put("sign", request.sign);
            if (!mTitle.getText().toString().equals(name)) {
                data.put("name", mTitle.getText().toString());
            }
            if (!mIntro.getText().toString().equals(intro)) {
                data.put("info", mIntro.getText().toString());
            }

            data.put("broadcastId", broadcastId);
            if (null != coverFile) {
                mEditPresenter.onBroadcastEdit(data, coverFile);
            } else {
                mEditPresenter.onBroadcastEdit(data);
            }
        } else {
            toastShort("播单标题不能为空");
        }
    }

    @OnClick({R.id.btn_left, R.id.tv_right, R.id.imv_cover_edit, R.id.tv_change_cover_edit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_right:
                doEditBroadcast();
                break;
            case R.id.imv_cover:
            case R.id.tv_change_cover:
                takePhoto.onPickFromGallery();
                break;
            default:
                break;
        }
    }


}
