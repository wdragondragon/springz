package org.jdragon.springz.test.domain;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 00:38
 * @Description:
 */
public class User {

    private String username;


    private String password;


    private String tel;


    private String birth;


    private boolean vip;

    public User() {
    }

    public User(String username, String password, String tel, String birth, Boolean vip) {
        this.username = username;
        this.password = password;
        this.tel = tel;
        this.birth = birth;
        this.vip = vip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", tel='" + tel + '\'' +
                ", birth='" + birth + '\'' +
                ", Vip=" + vip +
                '}';
    }
}
