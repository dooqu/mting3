package cn.xylink.mting.speech;

import android.telephony.ServiceState;

import java.lang.ref.WeakReference;

public class SpeechSettingService {
    WeakReference<SpeechService> speechServiceWeakReference;

    private SpeechSettingService(SpeechService speechService) {
        speechServiceWeakReference = new WeakReference<>(speechService);
    }

    public static SpeechSettingService create(SpeechService service) {
        if(service != null) {
            return new SpeechSettingService(service);
        }
        return null;
    }

    public Speechor.SpeechorRole getRole() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            return speechServiceWeakReference.get().getRole();
        }
        return null;
    }


    public void setRole(Speechor.SpeechorRole role) {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            speechServiceWeakReference.get().setRole(role);
        }
    }

    public Speechor.SpeechorSpeed getSpeed() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            return speechServiceWeakReference.get().getSpeed();
        }
        return null;
    }

    public void setRole(Speechor.SpeechorSpeed speed) {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            speechServiceWeakReference.get().setSpeed(speed);
        }
    }

    public SpeechService.SpeechServiceState getState() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            return speechServiceWeakReference.get().getState();
        }
        return null;
    }
}
