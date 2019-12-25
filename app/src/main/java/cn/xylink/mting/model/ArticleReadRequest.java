package cn.xylink.mting.model;

public class ArticleReadRequest extends  ArticleInfoRequest {
    private int read;
    private float progress;

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
