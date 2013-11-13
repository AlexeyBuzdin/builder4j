package com.abuzdin.builder4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class Builder4JAnnotations {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface InjectProxy {}

    public static void initProxies(Object testClass) {
        Class<?> clazz = testClass.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.isAnnotationPresent(InjectProxy.class)) {
                    Class<?> fieldClass = field.getType();
                    Object proxy = TestBuilder.proxyBean(fieldClass);
                    field.setAccessible(true);
                    field.set(testClass, proxy);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
