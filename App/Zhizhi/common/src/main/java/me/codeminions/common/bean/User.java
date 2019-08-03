package me.codeminions.common.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class User implements Serializable {
    @Expose
    private String name;
    @Expose
    private int sex;

    @Expose
    private String author_des;

    @Expose
    private String pic_address;

    public User(String name, String author_des) {
        this.name = name;
        this.author_des = author_des;
    }

    public User(String name, int sex, String author_des, String pic_address) {
        this.name = name;
        this.sex = sex;
        this.author_des = author_des;
        this.pic_address = pic_address;
    }

    public String getAuthor_des() {
        return author_des;
    }

    public void setAuthor_des(String author_des) {
        this.author_des = author_des;
    }

    public String getPic_address() {
        return pic_address;
    }

    public void setPic_address(String pic_address) {
        this.pic_address = pic_address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public int getSex() {
        return sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", author_des='" + author_des + '\'' +
                ", pic_address='" + pic_address + '\'' +
                '}';
    }
}
