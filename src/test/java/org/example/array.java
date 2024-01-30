package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class array {

    private static String[] blacklist = new String[]{"org.springframework","jakarta.servlet","javax.servlet","javax.ws"};

    public static void main(String[] args) {
        ArrayList<String> s = new ArrayList<>();
        s.add("o");
        s.add("j");
        s.add("b");
        s.add("k");
        //m(s);
        String a = "org.springframework.a.*";
        System.out.println(Arrays.stream(blacklist).anyMatch(str -> a.contains(str)));
        HashMap<String,String> c1 = new HashMap<>();
        c1.put("1","2");
        c1.put("2","2");
        c1.put("3","2");
        c1.put("4","2");
        c1.put("5","2");
        String t = "3";
        System.out.println(Arrays.stream(c1.keySet().toArray(new String[0])).anyMatch(str -> str.contains(t)));
        Date d = new Date();
        //String  l = d.getTime();


    }
    public static void m(Object c)
    {

        System.out.println((ArrayList<String>)c);
    }
}
