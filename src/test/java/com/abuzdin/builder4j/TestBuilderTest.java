package com.abuzdin.builder4j;

import org.junit.Before;
import org.junit.Test;

import static com.abuzdin.builder4j.Builder4JAnnotations.InjectProxy;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class TestBuilderTest {

    @InjectProxy
    TestBean bean;

    @InjectProxy
    ChildBean childBean;

    @Before
    public void setUp() {
        Builder4JAnnotations.initProxies(this);
    }

    @Test
    public void shouldBuildNewInstance() {
        bean = TestBuilder.forBean(TestBean.class)
                .build();

        assertThat(bean, notNullValue());
    }

    @Test
    public void shouldSetStringField() {
        bean = TestBuilder.forBean(TestBean.class, bean)
                .with(bean.getStringField(), "Hello World")
                .build();

        assertThat(bean, hasProperty("stringField", is("Hello World")));
    }

    @Test
    public void shouldSetTwoFieldsInChain() {
        bean = TestBuilder.forBean(TestBean.class, bean)
                .with(bean.getStringField(), "Hello World")
                .with(bean.getIntField(), 1)
                .build();

        assertThat(bean, allOf(
                hasProperty("stringField", is("Hello World")),
                hasProperty("intField", is(1))
        ));
    }

    @Test
    public void shouldBuildNewInstanceWithoutProxy() {
        TestBean bean = TestBuilder.forBean(TestBean.class)
                .build();

        assertThat(bean, notNullValue());
    }

    @Test
    public void shouldSetStringFieldWithoutProxy() {
        TestBean bean = TestBuilder.forBean(TestBean.class)
                .withField("stringField", "Hello World")
                .build();

        assertThat(bean, hasProperty("stringField", is("Hello World")));
    }

    @Test
    public void shouldSetTwoFieldsInChainWithoutProxy() {
        TestBean bean = TestBuilder.forBean(TestBean.class)
                .withField("stringField", "Hello World")
                .withField("intField", 1)
                .build();

        assertThat(bean, allOf(
                hasProperty("stringField", is("Hello World")),
                hasProperty("intField", is(1))
        ));
    }

    @Test
    public void shouldSetNestedFields() {
        bean = TestBuilder.forBean(TestBean.class, bean)
                .with(bean.getChildBean(), TestBuilder.forBean(ChildBean.class, childBean)
                        .with(childBean.getIntField(), 1)
                        .build())
                .build();

        assertThat(bean, hasProperty("childBean", hasProperty("intField", is(1))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForNullProxy() {
        TestBuilder.forBean(TestBean.class, null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForProxyNotImplementingHasProxyHandler() {
        TestBuilder.forBean(TestBean.class, new TestBean()).build();
    }
}