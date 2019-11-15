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
    View speechPanelView;
    boolean isPlaying;
    boolean isAttached;


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
        createPanelView();
        if(EventBus.getDefault().isRegistered(this) == false) {
            EventBus.getDefault().register(this);
        }
        isAttached = true;
        return true;
    }

    protected void createPanelView() {
        speechPanelView = View.inflate(contextRef.get(), R.layout.view_control_panel, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        contextRef.get().addContentView(speechPanelView, layoutParams);

        View icoPanelCollapse = speechPanelView.findViewById(R.id.ico_panel_collapse);
        icoPanelCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechPanelDialog dialog = new SpeechPanelDialog(contextRef.get(), speechServiceWeakReference.get());
                dialog.show();
            }
        });

        View icoPanelStatus = speechPanelView.findViewById(R.id.btn_panel_status);
        icoPanelStatus.setOnClickListener(new View.OnClickListener() {
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

        ImageView iconPanelClose = speechPanelView.findViewById(R.id.icon_panel_close);
        iconPanelClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechPanelView.setVisibility(View.INVISIBLE);
            }
        });
    }


    public void update(SpeechEvent... events) {
        SpeechService speechService = speechServiceWeakReference.get();
        if(speechService == null) {
            return;
        }
        speechPanelView.setVisibility(View.VISIBLE);
        ImageView statusIcon = speechPanelView.findViewById(R.id.ico_panel_status);
        TextView articleTitle = speechPanelView.findViewById(R.id.tv_panel_title);
        TextView broadcastTitle = speechPanelView.findViewById(R.id.tv_panel_broadcast_title);
        ProgressBar progressBar = speechPanelView.findViewById(R.id.progressbar_panel);
        ImageView closeIcon = speechPanelView.findViewById(R.id.icon_panel_close);
        closeIcon.setVisibility(speechService.getState() == SpeechService.SpeechServiceState.Paused? View.VISIBLE : View.GONE);

        SpeechEvent event = events.length > 0? events[0] : null;
        Article article = speechService.getSelected();
        if(article != null) {
            articleTitle.setText(article.getTitle());
            broadcastTitle.setText(article.getBroadcastId());
        }
        else if(event != null && event instanceof SpeechSerieLoaddingEvent) {
            articleTitle.setText(((SpeechSerieLoaddingEvent) event).getArticleTitle());
            broadcastTitle.setText(((SpeechSerieLoaddingEvent) event).getSerieTitle());
        }

        switch (speechService.getState()) {
            case Ready:
            case Playing:
                isPlaying = true;
                setProgressBar(false);
                statusIcon.setImageResource(R.mipmap.panel_pause);
                break;
            case Loadding:
                setProgressBar(true);
                statusIcon.setImageResource(R.mipmap.panel_pause);
                isPlaying = true;
                break;
            case Error:
            case Paused:
                isPlaying = false;
                setProgressBar(false);
                statusIcon.setImageResource(R.mipmap.panel_play);
                break;
        }
    }

    public void detach() {
        contextRef = null;
        speechServiceWeakReference = null;
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void setProgressBar(boolean display) {
        speechPanelView.findViewById(R.id.progressbar_panel).setVisibility(display? View.VISIBLE : View.INVISIBLE);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeechEvent(SpeechEvent event) {
        if(event instanceof SpeechProgressEvent) {
            setProgressBar(false);
        }
        else if(event instanceof SpeechBufferingEvent) {
            //如果是SpeechStartEvent 或者 BufferEvent，就显示loadding
            setProgressBar(true);
        }
        else if(event instanceof SpeechStartEvent) {
            setProgressBar(true);
        }
        else if(event instanceof SpeechSerieLoaddingEvent) {
            setProgressBar(true);
        }
        update(event);
    }
}
