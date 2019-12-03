package cn.xylink.mting.event;

/**
 * @author wjn
 * @date 2019/12/3
 */
public class ArticleFontSizeEvent {
    private String fontSize;

    public ArticleFontSizeEvent(String fontSize) {
        this.fontSize = fontSize;
    }

    public ArticleFontSizeEvent() {
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }
}
