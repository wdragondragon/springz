package org.jdragon.springz.test.domain;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.09.03 22:37
 * @Description:
 */

public class RobotPostOrder {

    private Integer id;

    private String order;

    private String url;

    private String param;

    //all or group or friend
    //0 or 1 or 2
    private int mode;

    //get or post
    //1 or 2
    private int request;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "RobotPostOrder{" +
                "id=" + id +
                ", order='" + order + '\'' +
                ", url='" + url + '\'' +
                ", param='" + param + '\'' +
                ", mode=" + mode +
                ", request=" + request +
                ", remark='" + remark + '\'' +
                '}';
    }
}
