package cn.xylink.mting.speech;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public abstract class SpeechServiceProxy {
    ServiceConnection connection;
    boolean connected;
    ContextWrapper context;
    boolean isBinding;

    public SpeechServiceProxy(ContextWrapper context) {
        this.context = context;
        this.connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                isBinding = false;
                SpeechServiceProxy.this.connected = true;
                onConnected(true, ((SpeechService.SpeechBinder) service).getService());
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBinding = false;
                SpeechServiceProxy.this.connected = false;
                onConnected(false, null);
            }
        };
    }


    public boolean bind() {
        if(isBinding == true) {
            return false;
        }
        isBinding = true;
        return this.context.bindService(new Intent(context, SpeechService.class), this.connection, context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        if (connected) {
            this.context.unbindService(this.connection);
        }
        this.context = null;
        this.connection = null;
        this.connected = false;
        this.isBinding = false;
    }

    protected abstract void onConnected(boolean connected, SpeechService service);

    public boolean isBinding() {
        return this.isBinding;
    }
}
