package com.abuzdin.builder4j;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class TestBuilderTest {

    @Test
    public void shouldBuildNewInstance() {
        TestBean bean = TestBuilder.forBean(TestBean.class)
                .build();

        assertThat(bean, notNullValue());
    }

    @Test
    public void shouldSetStringFieldWithSetter() {
        TestBean proxy = TestBuilder.proxyBean(TestBean.class);
        TestBean bean = TestBuilder.forBean(TestBean.class, proxy)
                .with(proxy.getStringField(), "Hello World")
                .build();

        assertThat(bean, hasProperty("stringField", is("Hello World")));
    }

    @Test
    public void shouldSetTwoFieldsInChain() {
        TestBean bean = TestBuilder.forBean(TestBean.class)
                .with("stringField", "Hello World")
                .with("intField", 1)
                .build();

        assertThat(bean, allOf(
                hasProperty("stringField", is("Hello World")),
                hasProperty("intField", is(1))
        ));
    }
}