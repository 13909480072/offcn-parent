package com.offcn.common.single;

//单例模式  实例 对象  懒汉模式
public class SingleObject2 {

    //定义一个变量 用来存放唯一的对象  饿汗模式
    public  static SingleObject2 obj = new SingleObject2();

    //封装构造方法
    private SingleObject2(){}

    //工厂方法
    private static SingleObject2 getInstance(){

        return new SingleObject2();
    }
}
