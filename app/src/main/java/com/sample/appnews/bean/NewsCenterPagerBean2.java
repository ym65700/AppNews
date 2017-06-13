package com.sample.appnews.bean;


import java.util.List;

/**
 * 作者：Administrator on 2017/6/3 12:15
 * 作用：
 */

public class NewsCenterPagerBean2 {
    private List<DataBean> data;
    private int retcode;

    public List<DataBean> getDataBeans() {
        return data;
    }

    public void setDataBeans(List<DataBean> data) {
        data = data;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    @Override
    public String toString() {
        return "NewsCenterPagerBean2{" +
                "DataBeans=" + data +
                ", retcode='" + retcode + '\'' +
                '}';
    }

    public static  class DataBean {
        private List<ChildrenBean> childrenBeans;
        private int id;
        private String title;
        private int type;
        private String url;
        private String url1;
        private String dayurl;
        private String excurl;
        private String weekurl;

        public List<ChildrenBean> getChildrenBeens() {
            return childrenBeans;
        }

        public void setChildrenBeens(List<ChildrenBean> childrenBeens) {
            this.childrenBeans = childrenBeens;
        }

        public String getWeekurl() {
            return weekurl;
        }

        public void setWeekurl(String weekurl) {
            this.weekurl = weekurl;
        }

        public String getExcurl() {
            return excurl;
        }

        public void setExcurl(String excurl) {
            this.excurl = excurl;
        }

        public String getDayurl() {
            return dayurl;
        }

        public void setDayurl(String dayurl) {
            this.dayurl = dayurl;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "childrenBeens=" + childrenBeans +
                    ", id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", url1='" + url1 + '\'' +
                    ", dayurl='" + dayurl + '\'' +
                    ", excurl='" + excurl + '\'' +
                    ", weekurl='" + weekurl + '\'' +
                    '}';
        }
    }

    public static  class ChildrenBean {
        private int id;
        private String title;
        private int type;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "ChildrenBean{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
