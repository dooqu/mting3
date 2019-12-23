package cn.xylink.mting.contract;

import cn.xylink.mting.bean.LinkArticle;
import cn.xylink.mting.bean.LinkCheckRequest;

/**
 * -----------------------------------------------------------------
 * 2019/12/19 14:45 : Create LinkCheckContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface LinkCheckContact {

    interface ILinkCheckView extends IBaseView {
        void onLinkCheckSuccess(LinkArticle data);

        void onLinkCheckError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void checkLink(LinkCheckRequest request);
    }
}
