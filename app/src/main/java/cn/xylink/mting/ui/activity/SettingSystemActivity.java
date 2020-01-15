package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.ui.dialog.BottomAccountLogoutDialog;
import cn.xylink.mting.utils.ContentManager;

/**
 * @author wjn
 * @date 2019/10/30
 */
public class SettingSystemActivity extends BasePresenterActivity {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_setting_sysytem);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("系统设置");
    }

    @OnClick({R.id.btn_left, R.id.ll_voice, R.id.ll_time, R.id.ll_font, R.id.ll_add, R.id.btn_out})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.ll_voice:
                startActivity(new Intent(this, SettingVoiceActivity.class));
                break;
            case R.id.ll_time:
                startActivity(new Intent(this, SettingTimerActivity.class));
                break;
            case R.id.ll_font:
                startActivity(new Intent(this, SettingFontActivity.class));
                break;
            case R.id.ll_add:

                break;
            case R.id.btn_out:
                if (ContentManager.getInstance().getVisitor().equals("0")) {//表示是游客登录
                    SettingSystemActivity.this.sendBroadcast(new Intent("action2exit"));
                } else {
                    showOutAccountDialog();
                }
                break;
            default:

                break;
        }
    }

    private void showOutAccountDialog() {
        BottomAccountLogoutDialog dialog = new BottomAccountLogoutDialog(this);
        dialog.onClickListener(new BottomAccountLogoutDialog.OnBottomSelectDialogListener() {
            @Override
            public void onFirstClick() {
                sendBroadcast(new Intent("action2exit"));
            }

            @Override
            public void onSecondClick() {
                TCAgent.onEvent(SettingSystemActivity.this, "account_exit");
                ContentManager.getInstance().setUserInfo(null);
                ContentManager.getInstance().setLoginToken("");
                ContentManager.getInstance().setVisitor("0");//设置成游客
                Intent intents = new Intent(SettingSystemActivity.this, LoginActivity.class);
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intents.putExtra(LoginActivity.LOGIN_ACTIVITY, "outAccount");
                startActivity(intents);
                SettingSystemActivity.this.finish();
            }
        });
        dialog.show();
    }

}
