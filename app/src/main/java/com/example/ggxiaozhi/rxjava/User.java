package com.example.ggxiaozhi.rxjava;

/**
 * 工程名 ： Rxjava
 * 包名   ： com.example.ggxiaozhi.rxjava
 * 作者名 ： 志先生_
 * 日期   ： 2017/10/8
 * 功能   ：
 */

public class User {

    String name;
    String age;

    public User() {

    }

    public User(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
