package cn.xylink.mting.ui.activity;

import android.support.v7.widget.AppCompatSeekBar;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.L;

/**
 * @author wjn
 * @date 2019/11/5
 */
public class SettingFontActivity extends BasePresenterActivity implements SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.sb_font_setting)
    AppCompatSeekBar mSeekBar;
    @BindView(R.id.rb_font_setting_1)
    RadioButton m1RadioButton;
    @BindView(R.id.rb_font_setting_2)
    RadioButton m2RadioButton;
    @BindView(R.id.rb_font_setting_3)
    RadioButton m3RadioButton;
    @BindView(R.id.tv_font_setting_type)
    TextView mTypeTextView;
    @BindView(R.id.tv_font_setting_show_title)
    TextView mShowTitleTextView;
    @BindView(R.id.tv_font_setting_show_content)
    TextView mShowContentTextView;


    @Override
    protected void preView() {
        setContentView(R.layout.activity_setting_font);
    }

    @Override
    protected void initView() {
        mSeekBar.setOnSeekBarChangeListener(this);
        setMode(ContentManager.getInstance().getTextSize());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("字体大小");
    }

    @OnClick(R.id.btn_left)
    void onClick(View view) {
        this.finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress<10){
            setMode(0);
            ContentManager.getInstance().setTextSize(0);
        }else if (progress>20){
            setMode(2);
            ContentManager.getInstance().setTextSize(2);
        }else {
            setMode(1);
            ContentManager.getInstance().setTextSize(1);
        }
    }


    private void setMode(int type){
        switch (type){
            case 0:
                m1RadioButton.setChecked(true);
                mTypeTextView.setText("默认");
                mShowTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                mShowContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//            tvArticleContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;
            case 1:
                m2RadioButton.setChecked(true);
                mTypeTextView.setText("中号");
                mShowTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
                mShowContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
//            tvArticleContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                break;
            case 2:
                m3RadioButton.setChecked(true);
                mTypeTextView.setText("大号");
                mShowTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                mShowContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            tvArticleContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
