package com.offcn.common.single;

//单例模式  实例 对象  懒汉模式
public class SingleObject {

    //定义一个变量 用来存放唯一的对象
    private static SingleObject obj;

    //封装构造方法
    private SingleObject(){}

    //工厂方法
    public static SingleObject getInstance(){
        if (obj == null){ //提高执行效率
            synchronized (SingleObject.class){//保证线程安全
                if (obj == null){//确保对象唯一
                    obj = new SingleObject();
                }
            }
        }
        return obj;
    }
}
