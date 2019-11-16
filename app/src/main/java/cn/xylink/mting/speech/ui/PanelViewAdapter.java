package cn.xylink.mting.speech.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import cn.xylink.mting.R;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.event.SpeechBufferingEvent;
import cn.xylink.mting.speech.event.SpeechEvent;
import cn.xylink.mting.speech.event.SpeechProgressEvent;
import cn.xylink.mting.speech.event.SpeechSerieLoaddingEvent;
import cn.xylink.mting.speech.event.SpeechStartEvent;
import cn.xylink.mting.speech.event.SpeechStopEvent;
import cn.xylink.mting.ui.activity.BaseActivity;
import cn.xylink.mting.ui.dialog.SpeechPanelDialog;

public class PanelViewAdapter {

    WeakReference<BaseActivity> contextRef;
    WeakReference<SpeechService> speechServiceWeakReference;

    boolean isPlaying;
    boolean isAttached;
    View speechPanelView;
    ImageView statusIcon;
    TextView articleTitle;
    TextView broadcastTitle ;
    ProgressBar progressBar;
    ImageView closeIcon ;

    @Deprecated
    public PanelViewAdapter(BaseActivity activity, SpeechService speechService) {
        attach(activity, speechService);
    }


    public PanelViewAdapter() {
    }

    public boolean attach(BaseActivity activity, SpeechService speechService) {
        if(isAttached || contextRef != null) {
            return false;
        }
        contextRef = new WeakReference<>(activity);
        speechServiceWeakReference = new WeakReference<>(speechService);
        onCreatePanelView();
        if(EventBus.getDefault().isRegistered(this) == false) {
            EventBus.getDefault().register(this);
        }
        isAttached = true;
        return true;
    }

    protected void onCreatePanelView() {
        speechPanelView = View.inflate(contextRef.get(), R.layout.view_control_panel, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
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
                SpeechPanelDialog dialog = new SpeechPanelDialog(contextRef.get(), speechServiceWeakReference.get());
                dialog.show();
            }
        });
        statusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contextRef.get() == null) {
                    return;
                }
                if(isPlaying) {
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
            }
        });
    }


    public void validatePanelView(SpeechEvent... events) {
        SpeechService speechService = speechServiceWeakReference.get();
        if(speechService == null) {
            return;
        }
        SpeechEvent event = events.length > 0? events[0] : null;
        if(event != null && event instanceof SpeechStopEvent) {
            return;
        }

        speechPanelView.setVisibility(View.VISIBLE);
        /*
        closeIcon.setVisibility(speechService.getState() == SpeechService.SpeechServiceState.Paused ||
                speechService.getState() == SpeechService.SpeechServiceState.Error? View.VISIBLE : View.GONE);
         */
        Article article = speechService.getSelected();
        SpeechService.SpeechServiceState currentState = speechService.getState();
        if(article != null) {
            articleTitle.setText(article.getTitle());
            broadcastTitle.setText(article.getBroadcastId());
        }
        else if(event != null && event instanceof SpeechSerieLoaddingEvent) {
            articleTitle.setText(((SpeechSerieLoaddingEvent) event).getArticleTitle());
            broadcastTitle.setText(((SpeechSerieLoaddingEvent) event).getSerieTitle());
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
            case Ready:
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
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void setPlayingState(boolean isPlaying) {
        this.isPlaying = isPlaying;
        statusIcon.setImageResource(isPlaying? R.mipmap.panel_pause : R.mipmap.panel_play);
    }

    private void displayLoaddingAnim(boolean display) {
        if(display) {
            if(progressBar.getVisibility() != View.VISIBLE) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeechEvent(SpeechEvent event) {
        validatePanelView(event);

        if(event instanceof SpeechProgressEvent) {
            displayLoaddingAnim(false);
        }
        else if(event instanceof SpeechBufferingEvent) {
            //如果是SpeechStartEvent 或者 BufferEvent，就显示loadding
            displayLoaddingAnim(true);
        }
        else if(event instanceof SpeechStartEvent) {
            //displayLoaddingAnim(true);
        }
        else if(event instanceof SpeechSerieLoaddingEvent) {
            //displayLoaddingAnim(true);
        }
        else if(event instanceof SpeechStopEvent) {
            if(((SpeechStopEvent) event).getStopReason() == SpeechStopEvent.StopReason.ListIsNull) {
                speechPanelView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
