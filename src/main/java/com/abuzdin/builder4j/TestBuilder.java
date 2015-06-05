package com.abuzdin.builder4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TestBuilder<BeanType> {

    private Supplier<BeanType> constructor;
    private Map<SetProperty<BeanType, Object>, Object> delayedSetters = new LinkedHashMap<>();

    /**
     * @param constructor  class that you create builder for
     * @param <BeanType>    type of instance built by builder
     * @return       new instance of builder for {@code constructor}
     */
    public static <BeanType> TestBuilder<BeanType> forBean(Supplier<BeanType> constructor) {
        return new TestBuilder<>(constructor);
    }

    private TestBuilder(Supplier<BeanType> constructor) {
        if (constructor == null) throw new NullPointerException();
        this.constructor = constructor;
    }

    /**
     * @return newly created object of type {@code <T>} with registered delayedSetters set to fields
     */
    public BeanType build() {
        try {
            BeanType beanType = constructor.get();
            setDelayedSetters(beanType);

            return beanType;
        } catch (Exception e) {
//            if (e instanceof InstantiationException || e instanceof IllegalAccessException) {
//                throw new RuntimeException("Failed to construct new instance of " + constructor.getName(), e);
//            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Should only
     *
     * @param setProperty  field value of dynamic proxy. Used only fot generic type inference
     * @param value       value to be registered for the field
//     * @param <F>         type of the registered field
//     * @param <V>         type of the registered value
     * @return            builder instance {@code this}
     */
    public <PropertyType> TestBuilder<BeanType> with(SetProperty<BeanType, PropertyType> setProperty, PropertyType value) {
        delayedSetters.put((SetProperty)setProperty, value);
        return this;
    }

    private void setDelayedSetters(BeanType o) {
        for (Map.Entry<SetProperty<BeanType, Object>, Object> entry : delayedSetters.entrySet()) {
            try {
                SetProperty<BeanType, Object> setter = entry.getKey();
                Object value = entry.getValue();
                setter.accept(o, value);
            } catch (Exception e) {
                throw new NoFieldFoundException("");
            }
        }
    }
}
