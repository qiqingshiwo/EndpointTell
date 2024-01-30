package com.qiqing.entity;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Base {

    public Base hasEndpointAnno(String className,String newString)
    {
        try {
            if(className.contains("POST") || className.contains("PUT") || className.contains("GET"))
            {
                className = "WsRsHttpMethod";
            }
            if(className.equals("PathVariable"))
            {
                className = "PathVariable";
            }
            if(className.equals("Path"))
            {
                className = "WsRsPath";
            }

            Class c = Class.forName("com.qiqing.entity." + className);
            Constructor constructor;
            if(newString != null)
            {
                constructor = c.getConstructor(String.class);
                return (Base)constructor.newInstance(newString);
            }else {
                constructor = c.getConstructor();
                return (Base)constructor.newInstance();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void invoke(Base Mapping,String key,Object value)
    {
        try{
            Class cz = Mapping.getClass();
            Field f = cz.getDeclaredField(key);
            f.setAccessible(true);
            Object fl = f.get(Mapping);

            if(f.getType().isAssignableFrom(String[].class))
            {
                List<String> ks =  Arrays.asList((String[])fl);
                ArrayList<String> ap = new ArrayList<>(ks);
                if(value instanceof String)
                {
                    if(!value.equals(null))
                    {
                        ap.add((String) value);
                    }
                }else if (value instanceof List)
                {
                    ArrayList<String> as = (ArrayList<String>) value;
                    for(String s : as)
                    {
                        ap.add(s);
                    }
                }
                f.set(Mapping,ap.toArray(new String[0]));
            }
            if(f.getType().isAssignableFrom(String.class))
            {

                if(value instanceof String)
                {
                    if(!value.equals(null))
                    {
                        f.set(Mapping,(String)value);
                    }
                }else if (value instanceof List)
                {
                    ArrayList<String> as = (ArrayList<String>) value;
                    for(String s : as)
                    {
                        f.set(Mapping,s);
                    }
                }
            }



//            if(value instanceof  String)
//            {
//                ap.add((String)value);
//            }else if(value instanceof List) {
//                ArrayList<String> as = (ArrayList<String>) value;
//                for(String s : as)
//                {
//                    ap.add(s);
//                }
//            }
        }catch (Exception e) {
            System.out.println(Mapping);
            System.out.println("mapping: " + Mapping);
            System.out.println("key: " + key);
            System.out.println("value: " + value);
            e.printStackTrace();
            //System.exit(0);
        }
    }
}
