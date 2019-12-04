package cn.xylink.mting.model.data;

import cn.xylink.mting.bean.UserInfo;

/**
 * @author wjn
 * @date 2019/12/4
 */
public class SmsLoginResponse {

    /**
     * code : 200
     * data : {"id":"20190703153759302547","userId":"20190703153759302547","phone":"15811372713","nickName":"听友2713","headImg":"http://download.test.xylink.cn/resource/M00/00/02/rBIACVtitQaAMt_RAACiGBzW3nI889.jpg","sex":0,"status":0,"regSource":"app","token":"app953be4a807854ed1858bde1ad06e25b1","createAt":1562139479303,"birthdate":null}
     * message : success
     * ext : {"newUser":0}
     */

    private int code;
    private UserInfo data;
    private String message;
    private ExtBean ext;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExtBean getExt() {
        return ext;
    }

    public void setExt(ExtBean ext) {
        this.ext = ext;
    }

    /*  public static class UserInfo {
     */

    /**
     * id : 20190703153759302547
     * userId : 20190703153759302547
     * phone : 15811372713
     * nickName : 听友2713
     * headImg : http://download.test.xylink.cn/resource/M00/00/02/rBIACVtitQaAMt_RAACiGBzW3nI889.jpg
     * sex : 0
     * status : 0
     * regSource : app
     * token : app953be4a807854ed1858bde1ad06e25b1
     * createAt : 1562139479303
     * birthdate : null
     *//*

        private String id;
        private String userId;
        private String phone;
        private String nickName;
        private String headImg;
        private int sex;
        private int status;
        private String regSource;
        private String token;
        private long createAt;
        private Object birthdate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getRegSource() {
            return regSource;
        }

        public void setRegSource(String regSource) {
            this.regSource = regSource;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getCreateAt() {
            return createAt;
        }

        public void setCreateAt(long createAt) {
            this.createAt = createAt;
        }

        public Object getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Object birthdate) {
            this.birthdate = birthdate;
        }
    }*/

    public static class ExtBean {
        /**
         * newUser : 0
         */

        private int newUser;

        public int getNewUser() {
            return newUser;
        }

        public void setNewUser(int newUser) {
            this.newUser = newUser;
        }
    }
}
