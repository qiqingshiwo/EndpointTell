package com.qiqing.parse;


import com.qiqing.entity.*;
import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sourceforge.pmd.lang.java.ast.*;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class JavaAnnotation {


    public String[] CanAnalysisAnnotaion = new String[]{"PutMapping","PostMapping","GetMapping","RequestMapping","RequestBody","RequestParam","RequestPart","Path","GET","POST","PUT","QueryParam","PathParam","FormParam","PathVariable"};

    public String[] CanAnalysisMapping = new String[]{"PutMapping","PostMapping","GetMapping","RequestMapping"};

    public String[] CanAnalysisWsPath = new String[]{"Path"};

    public String[] CanAnalysisRequest = new String[]{"RequestBody","RequestParam","RequestPart"};

    public String[] CanAnalysisWsRequest = new String[]{"POST"};


    public JSON SingleMemberAnnotaionAST(ASTSingleMemberAnnotation annotation)
    {
        Object bean = new String[]{};
        String annotationName = annotation.getAnnotationName();
        ASTMemberValue mv =  annotation.getMemberValue();
        String value = "";
        boolean isuRL = true;
        boolean isimage = true;
        JavaNode  node = mv.getChild(0).getChild(0).getChild(0);
        while (isimage)
        {
            value = node.getImage();
            if(value != "" && value != null)
            {
                isimage = false;
            }else {
                node = node.getChild(0);
            }
        }
        if(value.contains("\""))
        {
            isuRL = false;
        }
        value = value.replace("\"","");

        if(annotationName.contains("RequestParam"))
        {
            bean = new RequestParam(value);
        }
        else if(annotationName.contains("RequestBody"))
        {
            if(value.contains("false"))
            {
                bean = new RequestBody(false);
            }else {
                bean = new RequestBody();
            }
        }
        else if(annotationName.contains("RequestPart"))
        {
            bean = new RequestPart(value);
        }
        else if(annotationName.contains("Mapping"))
        {
            if(isuRL)
            {
                value = "automationNoCCC";
            }
            bean = new Mapping(annotationName,value);
        }
        else if(annotationName.contains("PathVariable"))
        {
            bean = new PathVariable(value);
        }
        else if(annotationName.contains("Param"))
        {
            bean = new WsRsParam(annotationName,value);
        }
        else if(annotationName.contains("Path"))
        {
            if(isuRL)
            {
                value = "automationNoCCC";
            }
            bean = new WsRsPath(value);
        }

//        else if(annotationName.contains("GET") || annotationName.contains("PUT") || annotationName.contains("POST"))
//        {
//            bean = new WsRsHttpMethod(annotationName);
//        }
        JSONObject json = JSONObject.fromObject(bean);
        return json;
    }

    public JSON NormalAnnotationAST(ASTNormalAnnotation annotation)
    {
        Object bean = new String[]{};
        HashMap<String,ArrayList<String>> hm = new HashMap();
        String annotationName = annotation.getAnnotationName();
        ASTMemberValuePairs mvp;
        if(annotation.getNumChildren()<2)
        {

        }else {
            mvp =  (ASTMemberValuePairs)annotation.getChild(1);
            for (Iterator<ASTMemberValuePair> it = mvp.iterator(); it.hasNext(); ) {
                ASTMemberValuePair mvpr = it.next();
                String mve = mvpr.getImage();

                ASTMemberValue amv = mvpr.getMemberValue();
                Object MemberValue = amv.getChild(0);
                ArrayList<String> values = new ArrayList<>();
                // 这个能解决 @RequestMapping(value={"UPLOAD","up"}) 此类问题
                if(MemberValue instanceof ASTMemberValueArrayInitializer)
                {
                    ASTMemberValueArrayInitializer memberValues = (ASTMemberValueArrayInitializer)MemberValue;
                    int ChildrenNum = memberValues.getNumChildren();
                    for(int num = 0; num < ChildrenNum; num ++)
                    {
                        ASTMemberValue astvalue = (ASTMemberValue)memberValues.getChild(num);
                        values.add(astvalue.getChild(0).getChild(0).getChild(0).getImage().replace("\"",""));
                    }
                }if(MemberValue instanceof ASTMemberValue)
                {
                    ASTMemberValue mbv = (ASTMemberValue) MemberValue;
                    values.add(mbv.getChild(0).getChild(0).getChild(0).getChild(0).getImage().replace("\"",""));
                }else {
                    values.add(amv.getChild(0).getChild(0).getChild(0).getImage());
                }
                hm.put(mve,values);
            }
        }
         // 正常来说,不会出现没有的情况,但是就是会有意外情况比如@GetMapping("home") @PostMapping()


        Base base = new Base();
        if(annotationName.contains("Mapping") || annotationName.contains("FormParam")  || annotationName.contains("QueryParam") || annotationName.contains("PathParam"))
        {
            base = base.hasEndpointAnno(annotationName.contains("Mapping") ? "Mapping" : "WsRsParam", annotationName);
            for(String key : hm.keySet())
            {
                base.invoke(base,key,hm.get(key));
            }
        }
        else if(annotationName.contains("GET") || annotationName.contains("POST") || annotationName.contains("PUT"))
        {
            base = base.hasEndpointAnno("WsRsHttpMethod", annotationName);
        }
        else {
            base = base.hasEndpointAnno(annotationName, null);
            for(String key : hm.keySet())
            {
                base.invoke(base,key,hm.get(key));
            }
        }
        bean = base;


//        if(annotationName.contains("Mapping"))
//        {
//            Mapping bs = new Mapping(annotationName);
//            for(String key : hm.keySet())
//            {
//                bs.invoke(bs.getClass(),key,hm.get(key));
//            }
//            bean = bs;
//        }
//        if(annotationName.contains("RequestBody"))
//        {
//            RequestBody rb = new RequestBody();
//            for(String key:hm.keySet())
//            {
//                rb.invoke(rb.getClass(),key,hm.get(key));
//            }
//            bean = rb;
//        }
//        if(annotationName.contains("RequestParam"))
//        {
//            RequestParam rp = new RequestParam();
//            for(String key:hm.keySet())
//            {
//                rp.invoke(rp.getClass(),key,hm.get(key));
//            }
//            bean = rp;
//        }
        JSONObject json = JSONObject.fromObject(bean);
        return json;
    }

    public JSON MarkerAnnotationAST(ASTMarkerAnnotation annotation)
    {
        Object bean = new String[]{};
        String annotationName = annotation.getAnnotationName();
        Base base = new Base();
        if(annotationName.contains("Mapping") )
        {
            base = base.hasEndpointAnno("Mapping", annotationName);
            bean = base;
        }
        else if(annotationName.contains("GET") || annotationName.contains("POST") || annotationName.contains("PUT"))
        {
            base = base.hasEndpointAnno("WsRsHttpMethod", annotationName);
            bean = base;
        }
        else {
            base = base.hasEndpointAnno(annotationName, null);
            bean = base;
        }
        JSONObject json = JSONObject.fromObject(bean);
        return json;
    }

    public JSON AnnotationAST(ASTAnnotation annotation)
    {
        String name = annotation.getAnnotationName();
        //System.out.println(name);
        JSON json = JSONObject.fromObject(new Object());
        if(!ArrayUtils.contains(CanAnalysisAnnotaion,name))
        {
            return null;
        }
        JavaNode anno = annotation.getChild(0);
        if(anno instanceof ASTMarkerAnnotation)
        {
            json = MarkerAnnotationAST((ASTMarkerAnnotation) anno);
        }else if(anno instanceof ASTNormalAnnotation)
        {
            json = NormalAnnotationAST((ASTNormalAnnotation) anno);
        } else if (anno instanceof ASTSingleMemberAnnotation) {
            json = SingleMemberAnnotaionAST((ASTSingleMemberAnnotation) anno);
        }


        if(Arrays.asList(CanAnalysisMapping).contains(name))
        {
            MorphDynaBean obj =  (MorphDynaBean) JSONSerializer.toJava(json);
            ArrayList<String> path = (ArrayList<String>) obj.get("path");
            ArrayList<String> value =(ArrayList<String>) obj.get("value");
            if(path.isEmpty())
            {
                obj.set("path",value);
            }else {
                obj.set("value",path);
            }
            ArrayList<String> al = (ArrayList<String>) obj.get("path");
            ArrayList<String> pv = new ArrayList<>();

            for(String as : al)
            {
                if(as == null)
                {
                    continue;
                }
                String temp = as.replace("\"","");
                if(!temp.startsWith("/"))
                {
                    temp = "/" +temp;
                }
                pv.add(temp);
            }
            if(al.isEmpty())
            {
                pv.add("/");
            }
            obj.set("path",pv);
            obj.set("value",pv);
            String mapping = (String) obj.get("annotation");
            if(!mapping.contains("Request"))
            {
                ArrayList<String> method = new ArrayList<>();
                String newmapping = mapping.replace("Mapping","").toUpperCase();
                method.add(newmapping);
                obj.set("annotation","RequestMapping");
                obj.set("method",method);
            }
            ArrayList<String> method = (ArrayList<String>) obj.get("method");
            ArrayList<String> newmethod = new ArrayList<>();
            for(String s : method)
            {
                if(s == null)
                {
                    continue;
                }
                newmethod.add(s.replace("RequestMethod.","").toUpperCase());
            }
            obj.set("method",newmethod);
            /**
             * 这里需要对"method":[]的情况进行处理,这代表这是个RequestMapping请求
             */
            ArrayList<String> nnewmethod = (ArrayList<String>) obj.get("method");
            if(nnewmethod.isEmpty())
            {
                nnewmethod  = new ArrayList<>(Arrays.asList(new String[]{"GET","POST"}));
                obj.set("method",nnewmethod);
            }
            json = JSONSerializer.toJSON(obj);
        }
        if(name.equals("RequestParam"))
        {
            MorphDynaBean obj =  (MorphDynaBean) JSONSerializer.toJava(json);
            String value =(String) obj.get("value");
            String pname = (String) obj.get("name");
            if(pname.isEmpty() && !value.isEmpty())
            {
                pname = value.replace("\"","");
            }if(value.isEmpty() && !pname.isEmpty())
            {
                value = pname.replace("\"","");
            }
            obj.set("name",pname);
            obj.set("value",value);
            json = JSONSerializer.toJSON(obj);
        }
//        if(Arrays.asList(CanAnalysisRequest).contains(name))
//        {
//            if(name.equals("RequestParam"))
//            {
//
//            }
//        }
        //System.out.println(json);

        return json;
    }

}
