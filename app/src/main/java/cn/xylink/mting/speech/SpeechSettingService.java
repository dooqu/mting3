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

    /*
    获取当前朗读服务的朗读角色
    如果朗读服务未启动，返回null
     */
    public Speechor.SpeechorRole getRole() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            return speechServiceWeakReference.get().getRole();
        }
        return null;
    }


    /*
    设定当前朗读服务的朗读角色
     */
    public void setRole(Speechor.SpeechorRole role) {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            speechServiceWeakReference.get().setRole(role);
        }
    }


    /*
    获取当前朗读服务的朗读速度
    如果朗读服务未启动，返回null
     */
    public Speechor.SpeechorSpeed getSpeed() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            return speechServiceWeakReference.get().getSpeed();
        }
        return null;
    }

    /*
    设定当前朗读服务的朗读速度
     */
    public void setSpeed(Speechor.SpeechorSpeed speed) {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            speechServiceWeakReference.get().setSpeed(speed);
        }
    }


    /*
    获取当前朗读服务的状态
    如果朗读服务未启动，返回null
     */
    public SpeechService.SpeechServiceState getState() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            return speechServiceWeakReference.get().getState();
        }
        return null;
    }


    /*
    获取当前朗读服务的倒计时模式
    CountDownMode.None : 无倒计时模式
    CountDownMode.Number : 按文章数量进行倒计时
    CountDownMode.Minutes : 按分钟时间进行倒计时
     */
    public SpeechService.CountDownMode getCountDownMode() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            return speechServiceWeakReference.get().getCountDownMode();
        }
        return SpeechService.CountDownMode.None;
    }


    /*
    返回朗读服务的倒计时阈值，
    如果当前倒计时模式为None，返回0
    如果当前倒计时模式为Number，返回读完指定篇文章后关闭的设定值
    如果当前倒计时模式为Minutes，返回读完指定的分钟数后关闭的设定值
     */
    public int getCountDownThresholdValue() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            return speechServiceWeakReference.get().getCountDownThresholdValue();
        }
        return 0;
    }


    /*
    返回朗读服务倒计时模式的更新值
     */
    public int getCountDownValue() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            return speechServiceWeakReference.get().getCountDownValue();
        }
        return 0;
    }


    /*
    如果朗读服务的倒计时设定为CountDownMode.Minutes，返回时间格式的倒计时更新值
     */
    public String getCountDownValueOfTimeFormat() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            return speechServiceWeakReference.get().getCountDownStringValue();
        }
        return "00:00";
    }


    /*
    对当前朗读服务进行倒计时设定
    mode 为倒计时模式
    countDownValue，为指定倒计时模式下的倒计时阈值
     */
    public void setCountDown(SpeechService.CountDownMode mode, int countDownValue) {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            speechServiceWeakReference.get().setCountDown(mode, countDownValue);
        }
    }


    /*
    取消朗读服务的倒计时
     */
    public void cancelCountDown() {
        if(speechServiceWeakReference != null && speechServiceWeakReference.get() != null) {
            speechServiceWeakReference.get().cancelCountDown();
        }
    }
}
