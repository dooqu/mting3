package cn.xylink.mting.event;

import android.app.Activity;

/**
 * @author wjn
 * @date 2019/12/3
 */
public class ArticleDetailScrollEvent {
    private String motion;
    private Activity activity;

    public ArticleDetailScrollEvent() {
        super();
    }

    public ArticleDetailScrollEvent(String motion) {
        this.motion = motion;
    }

    public String getMotion() {
        return motion;
    }

    public void setMotion(String motion) {
        this.motion = motion;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
