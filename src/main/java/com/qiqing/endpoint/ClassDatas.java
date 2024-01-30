package com.qiqing.endpoint;

import com.qiqing.entity.Classinfo;
import com.qiqing.entity.javaMethod;

import java.util.ArrayList;

public class ClassDatas {
    private static Classinfo info = new Classinfo();

    private static ArrayList<Classinfo> infos = new ArrayList<>();
    public static Classinfo getInfo() {
        return info;
    }

    public static ArrayList<Classinfo> getInfos()
    {
        return infos;
    }
    public static void addClassInfo(Classinfo classinfo)
    {
        infos.add(classinfo);
    }

    public static void addMethod(javaMethod method)
    {
        int x = infos.size() - 1;
        Classinfo info = infos.get(x);
        info.addMethod(method);
    }


}
