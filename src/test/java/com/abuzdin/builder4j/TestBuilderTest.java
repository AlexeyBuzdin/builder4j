package com.abuzdin.builder4j;

import org.junit.Before;
import org.junit.Test;

import static com.abuzdin.builder4j.Builder4JAnnotations.InjectProxy;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class TestBuilderTest {

    @InjectProxy
    TestBean proxy;

    @Before
    public void setUp() {
        Builder4JAnnotations.initProxies(this);
    }

    @Test
    public void shouldHaveLastAccessedFieldNotEmptyInProxy() {
        proxy.getIntField();
        String lastAccessedField = ((HasLastAccessedField) proxy).getLastAccessedField();

        assertThat(lastAccessedField, equalTo("intField"));
    }

    @Test
    public void shouldBuildNewInstance() {
        TestBean bean = TestBuilder.forBean(TestBean.class)
                .build();

        assertThat(bean, notNullValue());
    }

    @Test
    public void shouldSetStringFieldWithSetter() {
        TestBean bean = TestBuilder.forBean(TestBean.class, proxy)
                .with(proxy.getStringField(), "Hello World")
                .build();

        assertThat(bean, hasProperty("stringField", is("Hello World")));
    }

    @Test
    public void shouldSetTwoFieldsInChain() {
        TestBean bean = TestBuilder.forBean(TestBean.class, proxy)
                .with(proxy.getStringField(), "Hello World")
                .with(proxy.getIntField(), 1)
                .build();

        assertThat(bean, allOf(
                hasProperty("stringField", is("Hello World")),
                hasProperty("intField", is(1))
        ));
    }
}