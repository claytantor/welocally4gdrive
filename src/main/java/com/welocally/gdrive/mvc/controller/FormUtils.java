package com.welocally.gdrive.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;

import com.welocally.gdrive.domain.Index;

@Component
public class FormUtils {
    
    public Index deserializeIndex(String indexDecoded) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        
        String[] parts = indexDecoded.split("&");
        Map<String,Object> model = new HashMap<String,Object>();
        for (int i = 0; i < parts.length; i++) {
            String[] nv = parts[i].split("=");
            
            if(nv.length==1)
                nv= new String[]{nv[0],null};
            
            
            Object val = model.get(nv[0].substring(0, nv[0].length()-2));                
            if(nv[0].endsWith("[]")){
                String key = nv[0].substring(0, nv[0].length()-2);
                if(val != null)
                    model.put(key,val.toString()+","+nv[1]); 
                else
                    model.put(key, nv[1]); 
                
            } else {
                model.put(nv[0], nv[1]);    
            }
           
        }
        Index index = new Index();
        for (String propertyName : model.keySet()) {
            PropertyUtils.setProperty(index, propertyName,model.get(propertyName));
        }
        return index;
    }

}
