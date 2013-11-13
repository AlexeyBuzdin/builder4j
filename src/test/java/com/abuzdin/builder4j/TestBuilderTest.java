package com.abuzdin.builder4j;

import org.junit.Before;
import org.junit.Test;

import static com.abuzdin.builder4j.Builder4JAnnotations.InjectProxy;
import static com.abuzdin.builder4j.TestBeans.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class TestBuilderTest {

    @InjectProxy
    TestBean proxy;

    @InjectProxy
    ChildBean childBean;

    @Before
    public void setUp() {
        Builder4JAnnotations.initProxies(this);
    }

    @Test
    public void shouldBuildNewInstance() {
        TestBean bean = TestBuilder.forBean(TestBean.class)
                .build();

        assertThat(bean, notNullValue());
    }

    @Test
    public void shouldSetStringField() {
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
        TestBean bean = TestBuilder.forBean(TestBean.class, proxy)
                .with(proxy.getChildBean(), TestBuilder.forBean(ChildBean.class, childBean)
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

    @Test(expected = NullPointerException.class)
    public void shouldFailToConstructBuilderForNull() {
        TestBuilder.forBean(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToConstructProxyForNullAsClass() {
        TestBuilder.createProxy(null);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailToConstructProxyForClassWithNoDefaultConstructor() {
        TestBuilder.createProxy(NoDefaultConstructorBean.class);
    }

    @Test
    public void shouldConstructProxyForClassWithMultipleConstructors() {
        MultipleConstructorBean proxy = TestBuilder.createProxy(MultipleConstructorBean.class);
        assertThat(proxy, is(notNullValue()));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForFieldNameNull() {
        TestBuilder.forBean(TestBean.class).withField(null, "Hello World");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForFieldNameNullWithProxy() {
        TestBuilder.forBean(TestBean.class, proxy).with(null, "Hello World");
    }

    @Test(expected = NoFieldFoundException.class)
    public void shouldThrowNoFieldFoundExceptionForNonExistingFieldName() {
        TestBuilder.forBean(TestBean.class).withField("nonExistingField", "Hello World");
    }

    @Test(expected = NoFieldFoundException.class)
    public void shouldThrowNoFieldFoundExceptionForNonExistingFieldNameGetter() {
        TestBuilder.forBean(TestBean.class, proxy).with(proxy.getNonExistingField(), 1);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNonGetterMethod() {
        TestBuilder.forBean(TestBean.class, proxy).with(proxy.nonGetterMethod(), 1);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionIfNoProxyRegistered() {
        TestBuilder.forBean(TestBean.class).with(proxy.nonGetterMethod(), 1);
    }

    @Test
    public void shouldSetFieldIfUsingAlias() {
        TestBean bean = TestBuilder.forBean(TestBean.class, proxy)
                .with(proxy.getFieldWithOtherName(), 1)
                .build();

        assertThat(bean.getFieldWithOtherName(), is(1));
    }
}