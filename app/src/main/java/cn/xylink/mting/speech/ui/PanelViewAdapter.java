package cn.xylink.mting.speech.ui;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.lang.ref.WeakReference;

import cn.xylink.mting.MainActivity;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.event.ArticleDetailScrollEvent;
import cn.xylink.mting.event.StoreRefreshEvent;
import cn.xylink.mting.speech.SpeechError;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.event.SpeechBufferingEvent;
import cn.xylink.mting.speech.event.SpeechErrorEvent;
import cn.xylink.mting.speech.event.SpeechEvent;
import cn.xylink.mting.speech.event.SpeechPanelClosedEvent;
import cn.xylink.mting.speech.event.SpeechProgressEvent;
import cn.xylink.mting.speech.event.SpeechSerieLoaddingEvent;
import cn.xylink.mting.speech.event.SpeechStartEvent;
import cn.xylink.mting.speech.event.SpeechStopEvent;
import cn.xylink.mting.ui.activity.ArticleDetailActivity;
import cn.xylink.mting.ui.activity.BaseActivity;
import cn.xylink.mting.ui.dialog.SpeechPanelDialog;
import cn.xylink.mting.utils.DensityUtil;

public class PanelViewAdapter {
    static String TAG = PanelViewAdapter.class.getSimpleName();

    WeakReference<BaseActivity> contextRef;
    WeakReference<SpeechService> speechServiceWeakReference;

    boolean isPlaying;
    boolean isAttached;
    View speechPanelView;
    ImageView statusIcon;
    TextView articleTitle;
    TextView broadcastTitle;
    ProgressBar progressBar;
    ImageView closeIcon;
    Article currentArticle;
    Drawable drawablePlay;
    Drawable drawablePause;
    SpeechPanelDialog speechPanelDialog;

    public static boolean isUserClosed;

    @Deprecated
    public PanelViewAdapter(BaseActivity activity, SpeechService speechService) {
        attach(activity, speechService);
    }


    public PanelViewAdapter() {
    }

    public boolean attach(BaseActivity activity, SpeechService speechService) {
        if (isAttached || contextRef != null) {
            return false;
        }
        contextRef = new WeakReference<>(activity);
        speechServiceWeakReference = new WeakReference<>(speechService);
        onCreatePanelView();
        //create the panelView, and now do not display it until received the speech events.
        if (EventBus.getDefault().isRegistered(this) == false) {
            EventBus.getDefault().register(this);
        }
        isAttached = true;
        return true;
    }

    protected void onCreatePanelView() {
        drawablePause = contextRef.get().getDrawable(R.drawable.nsvg_play);
        drawablePlay = contextRef.get().getDrawable(R.drawable.nsvg_pause);

        speechPanelView = View.inflate(contextRef.get(), R.layout.view_control_panel, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        if (!(contextRef.get() instanceof MainActivity)) {
            speechPanelView.setPadding(speechPanelView.getPaddingLeft(), speechPanelView.getPaddingTop(), speechPanelView.getPaddingRight(), (contextRef.get() instanceof ArticleDetailActivity) ? DensityUtil.dip2pxComm(contextRef.get(), 88) : speechPanelView.getPaddingRight());
        }

        speechPanelView.setLayoutParams(layoutParams);
        contextRef.get().addContentView(speechPanelView, layoutParams);
        speechPanelView.setVisibility(View.INVISIBLE);

        statusIcon = speechPanelView.findViewById(R.id.ico_panel_status);
        articleTitle = speechPanelView.findViewById(R.id.tv_panel_title);
        broadcastTitle = speechPanelView.findViewById(R.id.tv_panel_broadcast_title);
        progressBar = speechPanelView.findViewById(R.id.progressbar_panel);
        closeIcon = speechPanelView.findViewById(R.id.icon_panel_close);
        View icoPanelCollapse = speechPanelView.findViewById(R.id.ico_panel_collapse);
        icoPanelCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(speechPanelDialog == null) {
                    speechPanelDialog = new SpeechPanelDialog(contextRef.get(), speechServiceWeakReference.get());
                }
                speechPanelDialog.show();
            }
        });
        statusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果服务SpeechService已经被回收掉， 那么重启服务，续播
                if (speechServiceWeakReference.get() == null || contextRef.get() == null) {
                    if (currentArticle != null) {
                        contextRef.get().postToSpeechService(currentArticle);
                    }
                    return;
                }
                if (isPlaying) {
                    speechServiceWeakReference.get().pause();
                }
                else {
                    speechServiceWeakReference.get().resume();
                }
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechPanelView.setVisibility(View.INVISIBLE);
                SpeechPanelClosedEvent event = new SpeechPanelClosedEvent();
                if (speechServiceWeakReference.get() != null) {
                    event.setArticle(speechServiceWeakReference.get().getSelected());
                }
                EventBus.getDefault().post(event);
            }
        });
    }


    public void validatePanelView(SpeechEvent... events) {
        SpeechService speechService = speechServiceWeakReference.get();
        if (speechService == null) {
            return;
        }

        SpeechEvent event = events.length > 0 ? events[0] : null;
        if (event != null && event instanceof SpeechStopEvent) {
            return;
        }
        isUserClosed = false;
        if (isScrollHidden == false || event instanceof SpeechStartEvent) {
            speechPanelView.setVisibility(View.VISIBLE);
        }

        Article article = speechService.getSelected();
        SpeechService.SpeechServiceState currentState = speechService.getState();
        Log.d(TAG, "setVisile, currentState=" + currentState);
        if (article != null) {
            articleTitle.setText(article.getTitle());
            broadcastTitle.setText("-1".equals(article.getBroadcastId()) ? "待读播单" : (article.getBroadcastTitle() != null ? article.getBroadcastTitle() : article.getBroadcastId()));
        }
        else if (event != null && event instanceof SpeechSerieLoaddingEvent && event.getArticle() != null) {
            Article eventArticle = event.getArticle();
            articleTitle.setText(eventArticle.getTitle());
            broadcastTitle.setText("-1".equals(eventArticle.getBroadcastId()) ? "待读播单" : eventArticle.getBroadcastTitle());
        }
        else {
            articleTitle.setText("正在加载...");
            broadcastTitle.setText("");
        }

        switch (currentState) {
            case Loadding:
                displayLoaddingAnim(true);
                //do not break.
            case Playing:
                closeIcon.setVisibility(View.GONE);
                setPlayingState(true);
                //ProgressEvent那里，消掉loadding，因为内部的buffering，也会调用该事件，但state == playing
                break;
            case Stoped:
            case Error:
            case Paused:
                closeIcon.setVisibility(View.VISIBLE);
                displayLoaddingAnim(false);
                setPlayingState(false);
                break;
        }
    }

    public void detach() {
        contextRef = null;
        speechServiceWeakReference = null;
        closeIcon = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void setPlayingState(boolean isPlaying) {
        setPlayingState(isPlaying, false);
    }

    private void setPlayingState(boolean isPlaying, boolean isInit) {
        this.isPlaying = isPlaying;
        Drawable destDrawable = isPlaying ? drawablePause : drawablePlay;
        boolean needChange = statusIcon.getDrawable() != destDrawable;
        if(needChange == true) {
            statusIcon.setImageDrawable(isPlaying ? drawablePause : drawablePlay);
            if(isInit == false) {
                ((Animatable)statusIcon.getDrawable()).start();
            }
        }
    }

    private void displayLoaddingAnim(boolean display) {
        if (display) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArticleFavorited(StoreRefreshEvent event) {
        if(speechServiceWeakReference.get() != null
                && speechServiceWeakReference.get().getSelected() != null
                && event.getArticleID().contains(speechServiceWeakReference.get().getSelected().getArticleId())) {
            speechServiceWeakReference.get().getSelected().setStore(event.getStroe());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeechEvent(SpeechEvent event) {
        Log.d(TAG, event.toString());
        validatePanelView(event);
        if (event instanceof SpeechStartEvent) {
            currentArticle = event.getArticle();
        }
        else if (event instanceof SpeechProgressEvent) {
            displayLoaddingAnim(false);
        }
        else if (event instanceof SpeechBufferingEvent) {
            //如果是SpeechStartEvent 或者 BufferEvent，就显示loadding
            displayLoaddingAnim(true);
        }
        else if (event instanceof SpeechSerieLoaddingEvent) {
            //displayLoaddingAnim(true);
        }
        else if (event instanceof SpeechStopEvent) {
            speechPanelView.setVisibility(View.INVISIBLE);
        }
        else if (event instanceof SpeechErrorEvent) {
            if (((SpeechErrorEvent) event).getErrorCode() == SpeechError.LIST_LOAD_ERROR) {
                speechPanelView.setVisibility(View.INVISIBLE);
            }
            Toast.makeText(contextRef.get(), ((SpeechErrorEvent) event).getMessage(), Toast.LENGTH_SHORT).show();
        }
        else if (event instanceof SpeechPanelClosedEvent) {
            isUserClosed = true;
            speechPanelView.setVisibility(View.INVISIBLE);
        }
    }


    boolean isScrollHidden = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArticleTextScrollEvent(ArticleDetailScrollEvent event) {
        if (contextRef.get() == null
//                || !(contextRef.get() instanceof ArticleDetailActivity)
                || speechServiceWeakReference.get() == null
                || speechServiceWeakReference.get().getState() == SpeechService.SpeechServiceState.Stoped
                || isUserClosed) {
            return;
        }

        switch (event.getMotion()) {
            case "glide":
                //downscroll内容向下滚动，显示panel
                speechPanelView.setVisibility(View.VISIBLE);
                isScrollHidden = false;
                break;

            case "upGlide":
                //内容向上滚动
                speechPanelView.setVisibility(View.INVISIBLE);
                isScrollHidden = true;
                break;
        }
    }
}
