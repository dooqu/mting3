package cn.xylink.mting.event;

/**
 * @author wjn
 * @date 2019/12/3
 */
public class ArticleDetailScrollEvent {
    private String motion;
    public ArticleDetailScrollEvent() {
        super();
    }

    public ArticleDetailScrollEvent(String motion) {
        this.motion = motion;
    }
}
