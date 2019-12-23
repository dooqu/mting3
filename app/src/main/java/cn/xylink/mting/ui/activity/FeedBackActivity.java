package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.tendcloud.tenddata.TCAgent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.contract.AddFeedbackContact;
import cn.xylink.mting.presenter.AddFeedbackPresenter;
import cn.xylink.mting.utils.FileUtil;
import cn.xylink.mting.utils.ImageUtils;
import cn.xylink.mting.utils.adapter.BaseAdapterHelper;
import cn.xylink.mting.utils.adapter.QuickAdapter;
import cn.xylink.multi_image_selector.MultiImageSelector;
import cn.xylink.multi_image_selector.MultiImageSelectorActivity;

public class FeedBackActivity extends BasePresenterActivity implements AddFeedbackContact.IAddFeedBackView, AdapterView.OnItemClickListener {

    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMG_DETAIL = 200;
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.sn_type)
    Spinner snType;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.gv_content)
    GridView gvContent;
    String aid;
    String url;

    private AddFeedbackPresenter addFeedbackPresenter;
    private QuickAdapter<String> mAdapter;
    private Integer sound = null;
    private Float speed = null;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_feedback);
    }

    @Override
    protected void initView() {
        addFeedbackPresenter = (AddFeedbackPresenter) createPresenter(AddFeedbackPresenter.class);
        addFeedbackPresenter.attachView(this);
        mAdapter = new QuickAdapter<String>(this, R.layout.item_feedback) {

            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                ImageView ivItem = helper.getView(R.id.iv_item);
                ImageView ivDelIco = helper.getView(R.id.iv_del_ico);
                helper.getView();
                if ("add".equals(item)) {
                    ImageUtils.get().load(ivItem, R.mipmap.ico_add);
                    ivDelIco.setVisibility(View.INVISIBLE);
                } else {
                    ivDelIco.setVisibility(View.VISIBLE);
                    ImageUtils.get().load(ivItem, new File(item));
                }
                ivDelIco.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.remove(helper.getPosition());
                        if (mAdapter.getCount() == 5 && !"add".equals(mAdapter.getItem(4))) {
                            mAdapter.add("add");
                        }
                    }
                });
            }
        };
        mAdapter.add("add");
        gvContent.setAdapter(mAdapter);
        gvContent.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String type = intent.getExtras().getString("type");
            aid = intent.getExtras().getString("aid");
            url = intent.getExtras().getString("url");
            sound = intent.getExtras().getInt("sound");
            speed = intent.getExtras().getFloat("speed");
            if ("detail".equals(type)) {
                String[] fadeType2 = getResources().getStringArray(R.array.fade_type2);
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fadeType2);
                snType.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("建议与反馈");
    }

    @OnClick(R.id.btn_left)
    void onBack(View v) {
        finish();
    }

    @OnClick(R.id.bt_submit)
    void onSubmit(View v) {
        TCAgent.onEvent(this, "sys_feedback");
        HttpParams param = new HttpParams();
        List<File> files = new ArrayList<>();
        FileUtil.clearImageCache(this);
        for (int i = 0; i < mAdapter.getCount(); i++) {
            String item = mAdapter.getItem(i);
            if ("add".equals(item)) {
                continue;
            }
            try {
                File file = new File(item);
                BufferedInputStream in = new BufferedInputStream(
                        new FileInputStream(file));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = 2;//宽和高变为原来的1/2，即图片压缩为原来的1/4
                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                String path = FileUtil.saveImage(this, bitmap);
                files.add(new File(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        param.put("articleId", aid);
        param.put("url", url);
        param.put("content", etContent.getText().toString());
        param.put("type", (String) snType.getSelectedItem());
        if (sound != null) {
            param.put("sound", sound);
        }
        if (speed != null) {
            param.put("speed", speed);
        }
        addFeedbackPresenter.onFeedBackForm(files, param);
    }

    @Override
    public void onAddFeedBackSuccess(BaseResponse<String> response) {
        toastCenterShort("反馈成功");
        finish();
    }

    @Override
    public void onBindCheckError(int code, String errorMsg) {
        toastShort("反馈失败");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<String> data = new ArrayList<>(mAdapter.getData());
        data.remove("add");
        if ("add".equals(mAdapter.getItem(position))) {
            MultiImageSelector.create(this)
                    .showCamera(false)
                    .count(6) // max select image size, 9 by default. used width #.multi()
                    .multi() // multi mode, default mode;
                    .origin(data) // original select data set, used width #.multi()
                    .start(this, REQUEST_IMAGE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("path", mAdapter.getItem(position));
            bundle.putInt("position", position);
            jumpActivityForResult(ImageDetailActivity.class, bundle, REQUEST_IMG_DETAIL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mAdapter.clear();
                mAdapter.addAll(path);
                if (mAdapter.getCount() < 6) {
                    mAdapter.add("add");
                }
            }
        } else if (requestCode == REQUEST_IMG_DETAIL) {
            if (resultCode == 100) {
                int position = data.getIntExtra("position", -1);
                if (position >= 0 && position < mAdapter.getCount()) {
                    mAdapter.remove(position);
                }
            }
        }
    }
}
