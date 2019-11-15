package cn.xylink.mting.ui.adapter;

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
import cn.xylink.mting.speech.event.SpeechEvent;
import cn.xylink.mting.speech.event.SpeechStopEvent;
import cn.xylink.mting.ui.activity.BaseActivity;
import cn.xylink.mting.ui.dialog.SpeechPanelDialog;

public class PanelViewAdapter {

    WeakReference<BaseActivity> contextRef;
    WeakReference<SpeechService> speechServiceWeakReference;
    View speechPanelView;
    int layout;
    boolean isPlaying;
    public PanelViewAdapter(BaseActivity activity, SpeechService speechService, int layoutId) {
        contextRef = new WeakReference<>(activity);
        speechServiceWeakReference = new WeakReference<>(speechService);
        layout = layoutId;
        createPanelView();

        EventBus.getDefault().register(this);
    }

    protected void createPanelView() {
        speechPanelView = View.inflate(contextRef.get(), layout, null);
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
        if(speechService == null
            || speechService.getSelected() == null) {
            return;
        }
        speechPanelView.setVisibility(View.VISIBLE);
        ImageView statusIcon = speechPanelView.findViewById(R.id.ico_panel_status);
        TextView articleTitle = speechPanelView.findViewById(R.id.tv_panel_title);
        TextView broadcastTitle = speechPanelView.findViewById(R.id.tv_panel_broadcast_title);
        ProgressBar progressBar = speechPanelView.findViewById(R.id.progressbar_panel);
        ImageView closeIcon = speechPanelView.findViewById(R.id.icon_panel_close);
        closeIcon.setVisibility(speechService.getState() == SpeechService.SpeechServiceState.Paused? View.VISIBLE : View.GONE);

        Article article = speechService.getSelected();
        articleTitle.setText(article.getTitle());
        broadcastTitle.setText(article.getBroadcastId());

        SpeechEvent event = events.length > 0? events[0] : null;

        switch (speechService.getState()) {
            case Ready:
            case Playing:
                isPlaying = true;
                progressBar.setVisibility(View.INVISIBLE);
                statusIcon.setImageResource(R.mipmap.panel_pause);
                break;
            case Loadding:
                progressBar.setVisibility(View.VISIBLE);
                statusIcon.setImageResource(R.mipmap.panel_pause);
                isPlaying = true;
                break;
            case Error:
            case Paused:
                isPlaying = false;
                progressBar.setVisibility(View.INVISIBLE);
                statusIcon.setImageResource(R.mipmap.panel_play);
                break;
        }
    }

    public void abandon() {
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeechEvent(SpeechEvent event) {
        update(event);
        if(event instanceof SpeechStopEvent) {
            speechPanelView.setVisibility(View.INVISIBLE);
        }
    }
}
