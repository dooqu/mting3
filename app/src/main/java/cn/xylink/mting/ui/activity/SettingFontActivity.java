package cn.xylink.mting.ui.activity;

import android.util.TypedValue;
import android.widget.TextView;

import butterknife.BindView;
import cn.xylink.mting.R;
import cn.xylink.mting.utils.ContentManager;

/**
 * @author wjn
 * @date 2019/11/5
 */
public class SettingFontActivity extends BasePresenterActivity {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_setting_font);
    }

    @Override
    protected void initView() {
        int textSize = 16;
        if (ContentManager.getInstance().getTextSize() == 1) {
            textSize = 21;
        }
        else if (ContentManager.getInstance().getTextSize() == 2) {
            textSize = 26;
        }
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("字体大小");
    }
}
