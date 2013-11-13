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

    /**
     * @param clazz  class that you create builder for
     * @param <T>    type of instance built by builder
     * @return       new instance of builder for {@code clazz}
     */
    public static <T> TestBuilder<T> forBean(Class<T> clazz) {
        return new TestBuilder<T>(clazz);
    }

    /**
     * @param clazz  class that you create builder for
     * @param proxy  dynamic proxy of {@code clazz} to register for new builder
     * @param <T>    type of instance built by builder
     * @return       new instance of builder for {@code clazz}
     */
    public static <T> TestBuilder<T> forBean(Class<T> clazz, T proxy) {
        TestBuilder<T> builder = forBean(clazz);
        builder.registerProxy(proxy);
        return builder;
    }

    /**
     * @param clazz  class dynamic proxy should extend
     * @param <T>    type of dynamic proxy to create
     * @return       new dynamic proxy for {@code clazz} that implements {@link HasProxyHandler}
     */
    public static <T> T createProxy(Class<T> clazz) {
        if(clazz == null) throw new NullPointerException("Class for proxy should not be null");

        try {
            T instance = clazz.newInstance();
            BeanProxyHandler answer = new BeanProxyHandler(instance);

            return mock(clazz, withSettings()
                    .defaultAnswer(answer)
                    .extraInterfaces(HasProxyHandler.class)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to construct new instance of " + clazz.getName(), e);
        }
    }

    private TestBuilder(Class<T> clazz) {
        if (clazz == null) throw new NullPointerException();

        this.clazz = clazz;
    }

    /**
     * @return newly created object of type {@code <T>} with registered values set to fields
     */
    public T build() {
        try {
            T instance = clazz.newInstance();
            setValues(instance);

            return instance;
        } catch (Exception e) {
            if (e instanceof InstantiationException || e instanceof IllegalAccessException) {
                throw new RuntimeException("Failed to construct new instance of " + clazz.getName(), e);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Should only be used with proxyHandler registered for the instance
     *
     * @param fieldValue  field value of dynamic proxy. Used only fot generic type inference
     * @param value       value to be registered for the field
     * @param <F>         type of the registered field
     * @param <V>         type of the registered value
     * @return            builder instance {@code this}
     */
    public <F, V extends F> TestBuilder<T> with(F fieldValue, V value) {
        if (proxyHandler != null) {
            String fieldName = proxyHandler.getLastAccessedField();
            if (!hasField(fieldName)) throw new NoFieldFoundException(fieldName);
            values.put(fieldName, value);
            return this;
        }
        throw new IllegalStateException("No ProxyHandler is registered for this builder");
    }

    /**
     * @param fieldName  field name to be registered
     * @param value      value to be registered for the field
     * @return           builder instance {@code this}
     */
    public TestBuilder<T> withField(String fieldName, Object value) {
        if (!hasField(fieldName)) throw new NoFieldFoundException(fieldName);
        values.put(fieldName, value);
        return this;
    }

    /**
     * @param proxy      dynamic proxy for {@code <T>} that implements {@link HasProxyHandler}
     * @return           builder instance {@code this}
     */
    public TestBuilder<T> registerProxy(T proxy) {
        if(proxy instanceof HasProxyHandler) {
            HasProxyHandler hasProxyHandler = (HasProxyHandler) proxy;
            this.proxyHandler = hasProxyHandler.getProxyHandler();
            return this;
        }
        throw new IllegalArgumentException("Parameter should implement HasProxyHandler and be not null");
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

    private boolean hasField(String fieldName) {
        try {
            clazz.getDeclaredField(fieldName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
