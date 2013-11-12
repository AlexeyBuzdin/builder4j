package com.abuzdin.builder4j;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class BuilderForTest {

    @Test
    public void shouldBuildNewInstance() {
        TestBean bean = new BuilderFor(TestBean.class).build();
        assertThat(bean, notNullValue());
    }

    @Test
    public void shouldSetStringFieldWithSetter() {
        TestBean bean = new BuilderFor(TestBean.class)
                .with("stringField", "Hello World")
                .build();
        assertThat(bean, hasProperty("stringField", is("Hello World")));
    }
}