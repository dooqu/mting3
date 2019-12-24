package cn.xylink.mting.speech;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import java.lang.ref.WeakReference;

public abstract class SpeechServiceProxy {
    ServiceConnection connection;
    boolean connected;
    boolean binding;
    boolean started;
    WeakReference<Context> contextWeakReference;

    public SpeechServiceProxy(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
        this.connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binding = false;
                connected = true;
                if(contextWeakReference.get() != null) {
                    onConnected(true, ((SpeechService.SpeechBinder) service).getService());
                }
                //occupyServiceHandle();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binding = false;
                connected = false;
                if(contextWeakReference.get() != null) {
                    onConnected(false, null);
                }
            }
        };
    }


    public synchronized boolean bind() {
        if(binding == true || connected == true || contextWeakReference.get() == null) {
            return false;
        }
        binding = true;
        Context context = contextWeakReference.get();
        return context.bindService(new Intent(context, SpeechService.class), this.connection, context.BIND_AUTO_CREATE);
    }

    public synchronized void unbind() {
        if (connected && contextWeakReference.get() != null) {
            contextWeakReference.get().unbindService(connection);
        }
        contextWeakReference.clear();;
        this.connection = null;
        this.connected = false;
        this.binding = false;
    }

    protected abstract void onConnected(boolean connected, SpeechService service);

    public boolean isConnected() {
        return connected;
    }

    public boolean isBinding() {
        return this.binding;
    }

    protected void occupyServiceHandle() {
        if(started ) {
            return;
        }
        Intent serviceIntent = new Intent(contextWeakReference.get(), SpeechService.class);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            contextWeakReference.get().startForegroundService(serviceIntent);
        }
        else {
            contextWeakReference.get().startService(serviceIntent);
        }
    }
}
