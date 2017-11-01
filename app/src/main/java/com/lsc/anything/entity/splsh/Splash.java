package com.lsc.anything.entity.splsh;

/**
 * Created by lsc on 2017/9/12 0012.
 *
 * @author lsc
 */

public class Splash {

    /**
     * data : {"enddate":"20170707","url":"http://www.bing.com/az/hprichbg/rb/Umbrella_ZH-CN8238029705_1920x1080.jpg","bmiddle_pic":"http://wx4.sinaimg.cn/bmiddle/006qRazely1fhazs9hhgqj30sg0g0h42.jpg","original_pic":"http://wx4.sinaimg.cn/large/006qRazely1fhazs9hhgqj30sg0g0h42.jpg","thumbnail_pic":"http://wx4.sinaimg.cn/thumbnail/006qRazely1fhazs9hhgqj30sg0g0h42.jpg"}
     * status : {"code":200,"message":""}
     */

    private DataBean data;
    private StatusBean status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * enddate : 20170707
         * url : http://www.bing.com/az/hprichbg/rb/Umbrella_ZH-CN8238029705_1920x1080.jpg
         * bmiddle_pic : http://wx4.sinaimg.cn/bmiddle/006qRazely1fhazs9hhgqj30sg0g0h42.jpg
         * original_pic : http://wx4.sinaimg.cn/large/006qRazely1fhazs9hhgqj30sg0g0h42.jpg
         * thumbnail_pic : http://wx4.sinaimg.cn/thumbnail/006qRazely1fhazs9hhgqj30sg0g0h42.jpg
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class StatusBean {
        /**
         * code : 200
         * message :
         */

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
