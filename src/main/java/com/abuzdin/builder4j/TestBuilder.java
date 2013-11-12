package com.abuzdin.builder4j;

import org.apache.commons.beanutils.BeanUtils;
import org.mockito.MockSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Proxy;
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

    public TestBuilder<T> with(String fieldName, Object value) {
        if (proxyHandler != null) {
            fieldName = proxyHandler.getLastAccessedField();
        }
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

    public TestBuilder<T> register(T proxy) {
        HasProxyHandler hasProxyHandler = (HasProxyHandler) proxy;
        this.proxyHandler = hasProxyHandler.getProxyHandler();
        return this;
    }
}
