package com.abuzdin.builder4j;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class BeanProxyHandler implements Answer {

    private String lastAccessedField;
    private Object realObject;

    public BeanProxyHandler(Object real) {
        realObject = real;
    }

    /**
     * @param invocation of mock method
     * @return           value of the real object, or mocked data
     * @throws Exception if method of real object fails to execute
     */
    @Override
    public Object answer(InvocationOnMock invocation) throws Exception {
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();

        if (isGetProxyHandler(method)) return this;
        if (isGetMethod(method)) lastAccessedField = extractAccessedFieldName(method);

        return method.invoke(realObject, arguments);
    }

    private boolean isGetProxyHandler(Method method) {
        Method[] methods = HasProxyHandler.class.getMethods();
        return methods[0].getName().equals(method.getName());
    }

    private boolean isGetMethod(Method method) {
        String name = method.getName();
        return name.startsWith("get") || name.startsWith("is");
    }

    private String extractAccessedFieldName(Method method) {
        try {
            Class<?> clazz = method.getDeclaringClass();
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : props) {
                if(method.equals(pd.getWriteMethod()) || method.equals(pd.getReadMethod())) {
                    return pd.getName();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getLastAccessedField() {
        return lastAccessedField;
    }
}
