package cn.xylink.mting.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
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

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.UserInfo;
import cn.xylink.mting.contract.UpdateUserInfoContact;
import cn.xylink.mting.contract.UploadHeadImgContact;
import cn.xylink.mting.model.UpdateUserRequset;
import cn.xylink.mting.model.UploadHeadImgRequest;
import cn.xylink.mting.presenter.UploadHeadImgPresenter;
import cn.xylink.mting.presenter.UploadUserInfoPresenter;
import cn.xylink.mting.ui.dialog.BottomSelectDialog;
import cn.xylink.mting.ui.dialog.BottomSelectSexDialog;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.DateUtils;
import cn.xylink.mting.utils.ImageUtils;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.utils.MD5;
import cn.xylink.mting.utils.SoftKeyBoardListener;

public class PersonalInfoActivity extends BasePresenterActivity implements TakePhoto.TakeResultListener, InvokeListener, UploadHeadImgContact.IUploadHeadImgView, BottomSelectDialog.OnBottomSelectDialogListener, UpdateUserInfoContact.IUpdateUserView {

    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.iv_my_head)
    ImageView ivhead;
    @BindView(R.id.et_nickName)
    EditText etNickName;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.iv_arrow_2)
    ImageView ivArrow3;

    @BindView(R.id.ll_root)
    LinearLayout ll_root;

    @BindView(R.id.tv_birthday)
    TextView tvBirthday;

    private TakePhoto takePhoto;

    private InvokeParam invokeParam;

    private UploadHeadImgPresenter headImgPresenter;
    private UploadUserInfoPresenter userInfoPresenter;

    private String headImgUrl;

    private int sex = -1;

    private InputMethodManager im;
    private String oldNiceName;
    private long birthday;
    TimePickerView pvTime;


    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void preView() {
        setContentView(R.layout.activity_personal_info);
    }


    public void setUserInfo() {
        UserInfo info = ContentManager.getInstance().getUserInfo();
        L.v(info);
        if (info != null) {
            if (!TextUtils.isEmpty(info.getHeadImg()))
                ImageUtils.get().loadCircle(ivhead, info.getHeadImg());
            if (!TextUtils.isEmpty(info.getNickName())) {
                tvNickName.setText(info.getNickName());
                etNickName.setText(info.getNickName());
                oldNiceName = info.getNickName();
            }
            if (info.getSex() == 0)
                tvSex.setText("男");
            else if (info.getSex() == 1)
                tvSex.setText("女");
            else
                tvSex.setText(R.string.please_choose);
        }
    }

    @Override
    protected void initView() {
        tvTitle.setText("个人信息");
        setUserInfo();
        viewTreeObserver();
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //键盘显示
                ivArrow3.setVisibility(View.INVISIBLE);
                tvNickName.setVisibility(View.INVISIBLE);
                etNickName.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                //键盘隐藏
                ivArrow3.setVisibility(View.VISIBLE);
                tvNickName.setVisibility(View.VISIBLE);
                etNickName.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(etNickName.getText()) && !oldNiceName.equals(etNickName.getText().toString())) {
                    tvNickName.setText(etNickName.getText());
                    oldNiceName = etNickName.getText().toString();
                    UpdateUserRequset request = new UpdateUserRequset();
                    request.setNickName(etNickName.getText().toString());
                    updateUser(request);
                }

            }
        });
    }

    private void viewTreeObserver() {
        final float screenHeight = getResources().getDisplayMetrics().heightPixels / 3;
        ll_root.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0
                        && (bottom - oldBottom > screenHeight)) {
                    ivArrow3.setVisibility(View.VISIBLE);
                    tvNickName.setVisibility(View.VISIBLE);
                    etNickName.setVisibility(View.INVISIBLE);

                    if (!TextUtils.isEmpty(etNickName.getText()) && !oldNiceName.equals(etNickName.getText().toString())) {
                        tvNickName.setText(etNickName.getText());
                        oldNiceName = etNickName.getText().toString();
                        UpdateUserRequset requset = new UpdateUserRequset();
                        requset.setSex(ContentManager.getInstance().getUserInfo().getSex());
                        requset.setNickName(etNickName.getText().toString());
                        updateUser(requset);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        headImgPresenter = (UploadHeadImgPresenter) createPresenter(UploadHeadImgPresenter.class);
        headImgPresenter.attachView(this);

        userInfoPresenter = (UploadUserInfoPresenter) createPresenter(UploadUserInfoPresenter.class);
        userInfoPresenter.attachView(this);

        UserInfo user = ContentManager.getInstance().getUserInfo();
        if (user == null)
            return;
        if (!TextUtils.isEmpty(user.getHeadImg()))
            ImageUtils.get().loadCircle(ivhead, headImgUrl);
        if (!TextUtils.isEmpty(user.getNickName()))
            tvNickName.setText(user.getNickName());
        if (user.getBirthdate() > 0)
            tvBirthday.setText(DateUtils.getDateText(new Date(user.getBirthdate()), DateUtils.YMD_BREAK));
    }

    @Override
    protected void initTitleBar() {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideSoftInput(etNickName);
    }

    @OnClick({R.id.btn_left,
            R.id.iv_my_head,
            R.id.iv_arrow_1,
            R.id.tv_sex,
            R.id.iv_arrow_2,
            R.id.iv_arrow_3,
            R.id.tv_nick_name,
            R.id.tv_birthday,
            R.id.iv_arrow_4,
            R.id.ll_nick_name,
            R.id.rv_sex,
            R.id.rv_birthday,
            R.id.rv_my_head
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                hideSoftInput(etNickName);
                finish();
                break;
            case R.id.iv_arrow_1:
            case R.id.iv_my_head:
            case R.id.rv_my_head:
                showPicSelectDialog();
                break;
            case R.id.iv_arrow_3:
            case R.id.tv_sex:
            case R.id.rv_sex:
                showSexSelectDialog();
                break;
            case R.id.ll_nick_name:
            case R.id.iv_arrow_2:
            case R.id.tv_nick_name:
                tvNickName.setVisibility(View.INVISIBLE);
                etNickName.setVisibility(View.VISIBLE);
                ivArrow3.setVisibility(View.INVISIBLE);
                etNickName.setSelection(etNickName.getText().length());
                etNickName.findFocus();
                etNickName.setFocusable(true);
                etNickName.setFocusableInTouchMode(true);
                etNickName.requestFocus();
                showSoftInput(etNickName);
                break;
            case R.id.rv_birthday:
            case R.id.tv_birthday:
            case R.id.iv_arrow_4:
                showDate();
                break;
        }
    }

    private void showDate() {

        Calendar data = Calendar.getInstance();

        if (!tvBirthday.equals("请选择")) {
            try {
                data.setTime(DateUtils.getDate(tvBirthday.getText().toString(), DateUtils.YMD_BREAK));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Calendar start = Calendar.getInstance();
        try {
            start.setTime(DateUtils.getDate("1900-01-01", DateUtils.YMD_BREAK));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = DateUtils.getDateText(date, DateUtils.YMD_BREAK);
                if (TextUtils.isEmpty(tvNickName.getText().toString()) && TextUtils.isEmpty(etNickName.getText().toString())) {
                    toastShort("昵称不能为空");
                    return;
                }
                birthday = date.getTime();
                tvBirthday.setText(time);
                UpdateUserRequset requset = new UpdateUserRequset();
                requset.setSex(ContentManager.getInstance().getUserInfo().getSex());
                requset.setNickName(etNickName.getText().toString());
                requset.setBirthdate(birthday);
                updateUser(requset);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setRangDate(start, Calendar.getInstance())
                .setDate(data)
                .build();

        pvTime.show();
    }


    public void hideSoftInput(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            etNickName.setVisibility(View.INVISIBLE);
            tvNickName.setVisibility(View.VISIBLE);
            tvNickName.setText(etNickName.getText().toString());
            UpdateUserRequset request = new UpdateUserRequset();
            request.setNickName(etNickName.getText().toString());
            updateUser(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showPicSelectDialog() {
        hideSoftInput(etNickName);
        BottomSelectDialog dialog = new BottomSelectDialog(this);
        dialog.setData(getResources().getString(R.string.take_pic), getResources().getString(R.string.select_album), this);
        dialog.show();
    }

    private void showSexSelectDialog() {
        hideSoftInput(etNickName);
        BottomSelectSexDialog dialog = new BottomSelectSexDialog(this);
        dialog.setData("男", "女", new BottomSelectSexDialog.OnBottomSelectDialogListener() {
            @Override
            public void onFirstClick() {
                sex = 0;
                dialog.dismiss();
                UpdateUserRequset requset = new UpdateUserRequset();
                requset.setNickName(ContentManager.getInstance().getUserInfo().getNickName());
                requset.setSex(sex);
                updateUser(requset);
            }

            @Override
            public void onSecondClick() {
                sex = 1;
                dialog.dismiss();
                UpdateUserRequset requset = new UpdateUserRequset();
                requset.setNickName(ContentManager.getInstance().getUserInfo().getNickName());
                requset.setSex(sex);

                updateUser(requset);
            }
        });
        dialog.show();
    }

    public void updateUser(UpdateUserRequset requset) {
        requset.doSign();
        userInfoPresenter.onUpdateUser(requset);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            String picPath = resultUri.toString().replace("file://", "");
            doUploadImg(new File(picPath));
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void doUploadImg(File file) {
        Map<String, String> data = new HashMap<>();
        UploadHeadImgRequest request = new UploadHeadImgRequest();
        data.put("token", request.token);
        data.put("timestamp", request.timestamp + "");
        String sign = MD5.md5crypt(BaseRequest.desKey + request.token + request.timestamp);
        data.put("sign", sign);
        headImgPresenter.uploadHeadImg(data, file);
    }

    private File picFile;

    @Override
    public void takeSuccess(TResult result) {
        String path;
        if (picFile != null) {
            path = picFile.toString();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(PersonalInfoActivity.this, type, invokeParam, this);
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
    public void onSuccessUploadHeadImg(BaseResponse<UserInfo> baseResponse) {
        headImgUrl = baseResponse.data.getHeadImg();

        if (TextUtils.isEmpty(headImgUrl)) {
            toastShort("上传头像失败");
        } else {
            ImageUtils.get().loadCircle(ivhead, headImgUrl);
            ContentManager.getInstance().setUserInfo(baseResponse.data);
        }
    }

    @Override
    public void onErrorUploadHeadImg(int code, String errorMsg) {
        if (null != ContentManager.getInstance().getUserInfo()) {
            if (TextUtils.isEmpty(ContentManager.getInstance().getUserInfo().getHeadImg())) {
                ImageUtils.get().loadCircle(ivhead, ContentManager.getInstance().getUserInfo().getHeadImg());
            }
        } else {
            ivhead.setImageResource(R.mipmap.personal_head);
        }
    }


    @Override
    public void onFirstClick() {
        picFile = new File(this.getExternalCacheDir(), System.currentTimeMillis() + ".png");
        Uri uri = Uri.fromFile(picFile);
        takePhoto.onPickFromCapture(uri);
    }

    @Override
    public void onSecondClick() {
        picFile = null;
        takePhoto.onPickFromGallery();
    }

    @Override
    public void onUpdateUserSuccess(BaseResponse<UserInfo> response) {
        L.v(response.data);
        UserInfo userInfo = ContentManager.getInstance().getUserInfo();
        switch (sex) {
            case 0:
                tvSex.setText(R.string.sex_man);
                userInfo.setSex(sex);
                break;
            case 1:
                tvSex.setText(R.string.sex_woman);
                userInfo.setSex(sex);
                break;
        }
        if (!TextUtils.isEmpty(etNickName.getText())) {
            userInfo.setNickName(etNickName.getText().toString());
        }
        if (birthday > 0)
            userInfo.setBirthdate(birthday);
        ContentManager.getInstance().setUserInfo(userInfo);
    }

    @Override
    public void onUpdateUserError(int code, String errorMsg) {
        L.v(code, errorMsg);
    }
}
