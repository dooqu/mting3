package cn.xylink.mting.ui.dialog;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * -----------------------------------------------------------------
 * 2019/11/15 15:12 : Create BottomTingItemModle.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BottomTingItemModle {
    private String name;
    private String nameTwo;
    private Drawable drawable;
    private Drawable drawableTwo;
    private boolean isTwo = false;
    private TextView view;
    private String id;

    public BottomTingItemModle() {
    }

    public BottomTingItemModle(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public BottomTingItemModle(String name, Drawable drawable, String id) {
        this.name = name;
        this.drawable = drawable;
        this.id = id;
    }

    public BottomTingItemModle(String name, Drawable drawable, boolean isTwo, String id) {
        this.name = name;
        this.drawable = drawable;
        this.isTwo = isTwo;
        this.id = id;
    }

    public BottomTingItemModle(String name, String nameTwo, Drawable drawable, Drawable drawableTwo, boolean isTwo) {
        this.name = name;
        this.nameTwo = nameTwo;
        this.drawable = drawable;
        this.drawableTwo = drawableTwo;
        this.isTwo = isTwo;
    }

    public BottomTingItemModle(String name, String nameTwo, Drawable drawable, Drawable drawableTwo, boolean isTwo, String id) {
        this.name = name;
        this.nameTwo = nameTwo;
        this.drawable = drawable;
        this.drawableTwo = drawableTwo;
        this.isTwo = isTwo;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameTwo() {
        return nameTwo;
    }

    public void setNameTwo(String nameTwo) {
        this.nameTwo = nameTwo;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Drawable getDrawableTwo() {
        return drawableTwo;
    }

    public void setDrawableTwo(Drawable drawableTwo) {
        this.drawableTwo = drawableTwo;
    }

    public boolean isTwo() {
        return isTwo;
    }

    public void setTwo(boolean two) {
        isTwo = two;
    }

    public TextView getView() {
        return view;
    }

    public void setView(TextView view) {
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
