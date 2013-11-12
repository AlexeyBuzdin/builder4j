package com.abuzdin.builder4j;

import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.Map;

public class TestBuilder<T> {

    private final Class<T> clazz;
    private Map<String, Object> values = new HashMap<String, Object>();

    public static <T> TestBuilder<T> forBean(Class<T> clazz) {
        return new TestBuilder<T>(clazz);
    }

    private TestBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T build() {
        try {
            T o = clazz.newInstance();
            setValues(o);

            return (T) o;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public TestBuilder<T> with(String fieldName, Object value) {
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
