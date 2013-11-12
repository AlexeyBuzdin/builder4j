package com.abuzdin.builder4j;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BuilderFor {

    private final Class clazz;
    private Map<String, Object> values = new HashMap<String, Object>();

    public <T> BuilderFor(Class<T> clazz) {
        this.clazz = clazz;
    }

    public <T> T build() {
        try {
            Object o = clazz.newInstance();
            setValues(o);

            return (T) o;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public BuilderFor with(String fieldName, Object value) {
        values.put(fieldName, value);
        return this;
    }

    private void setValues(Object o) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            try {
                BeanUtils.setProperty(o, entry.getKey(), entry.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
