package org.jdragon.springz.test.domain;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.28 10:26
 * @Description:
 */
public class Car {
    private String belong;

    public Car() {

    }

    public Car(String belong) {
        this.belong = belong;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    @Override
    public String toString() {
        return "Car{" +
                "belong='" + belong + '\'' +
                '}';
    }
}