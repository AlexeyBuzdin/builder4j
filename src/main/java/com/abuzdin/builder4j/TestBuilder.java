package com.abuzdin.builder4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class TestBuilder<T> {

    private Class<T> clazz;
    private BeanProxyHandler proxyHandler;
    private Map<String, Object> values = new HashMap<String, Object>();

    public static <T> TestBuilder<T> forBean(Class<T> clazz) {
        return new TestBuilder<T>(clazz);
    }

    public static <T> TestBuilder<T> forBean(Class<T> clazz, T proxy) {
        TestBuilder<T> builder = forBean(clazz);
        builder.register(proxy);
        return builder;
    }

    public static <T> T proxyBean(Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            BeanProxyHandler answer = new BeanProxyHandler(instance);

            return mock(clazz, withSettings()
                    .defaultAnswer(answer)
                    .extraInterfaces(HasProxyHandler.class)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TestBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T build() {
        try {
            T instance = clazz.newInstance();
            setValues(instance);

            return instance;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public <E, V extends E> TestBuilder<T> with(E field, V value) {
        String fieldName;
        if (proxyHandler != null) {
            fieldName = proxyHandler.getLastAccessedField();
        } else {
            fieldName = field.toString();
        }

        return withField(fieldName, value);
    }

    public TestBuilder<T> withField(String fieldName, Object value) {
        values.put(fieldName, value);
        return this;
    }

    public TestBuilder<T> register(T proxy) {
        HasProxyHandler hasProxyHandler = (HasProxyHandler) proxy;
        this.proxyHandler = hasProxyHandler.getProxyHandler();
        return this;
    }

    private void setValues(Object o) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            try {
                String fieldName = entry.getKey();
                Field field = o.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(o, entry.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
