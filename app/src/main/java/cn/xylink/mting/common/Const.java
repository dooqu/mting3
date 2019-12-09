package cn.xylink.mting.common;

/**
 * Created by liluhe on 2016/6/22.
 */
public class Const {
    //    public static String AID = "315041592E5359532E4444463031";
//103.226.132.194 端口9204
    public static final boolean isDebug = true;//日志等开发环境相关
    public static final boolean isTest = true;//开发中写死的一些东西
    public static final boolean isLog = true;//log日志
    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkrHpXQobL3MMEbZtHvojCwkDqx-4awVbmLqXRNXgk4K-FQQIA-Z0fnSJ3pZ_pfOe5eO12wBkhq-p4PGdU9MrBZz077HyEsUHSTyzpsnMMOaoRwSPuRMPK-GKOQ0zZGR_PEI1YDpBS4rakWeve0Pn2JwhKkmsO4KluKcfIukoR-wIDAQAB";//公钥

    //服务电话
    public static final String SERVICE_PHONE = "10086";
    /**
     * 版本编译时配置的接口请求地址
     **/
    public static final int LOC = 0;// 本地开发环境
    public static final int DEV = 1;// 开发环境
    public static final int TEST = 2;// 测试环境
    public static final int PRD = 3;// 生产环境
    public static final int UAT = 4;// UAT环境

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";


    // 表示公交卡充值类型交易记录
    public static final String ADDED = "1";
    // 表示公交卡消费类型交易记录
    public static final String USED = "2";
    public static final String CITY_CODE = "2711";
    public static final String WX_ID = "wx1ff8a65d066b2156";
    public static final String WX_SECRET = "e2f0ded1dbc3a9f586d13a74b55fe6c0";
    public static final String QQ_ID = "1107912298";
    public static final String WX_URL_BASE = "https://api.weixin.qq.com/sns/";
    public static final int PAGE_SIZE = 20;
    public static final String visitor = "visitor";

    public static String isUnReadFlag = "";//用于识别是否是分享过来的文章
    public static String nextPagePlay;
    public static final String PAGE_PLAY = "PAGE_PLAY";

    public static String TCAGENT_APPID = "FCAC8CE6C75F41C39A8F759E7FADB4F5";

    public static final String USERPROTOCOL_URL = "http://service.xylink.net/article/html/agreement.html";

    public class BottomDialogItem {
        public static final String SET_TOP = "置顶";
        public static final String CANEL_TOP = "取消置顶";
        public static final String SUBSCRIBE = "订阅";
        public static final String CANCEL_SUBSCRIBE = "取消订阅";
        public static final String DELETE = "删除";
        public static final String EDIT_BROADCAST = "编辑播单";
        public static final String REPORT = "举报";
        public static final String BATCH = "批量操作";
        public static final String FEEDBACK = "反馈";
        public static final String BROWER = "用其他浏览器打开";
    }

    public class SystemBroadcast {
        public static final String SYSTEMBROADCAST_UNREAD = "-1";
        public static final String SYSTEMBROADCAST_READED = "-2";
        public static final String SYSTEMBROADCAST_STORE = "-3";
        public static final String SYSTEMBROADCAST_MY_CREATE_ARTICLE = "-4";
    }


}
