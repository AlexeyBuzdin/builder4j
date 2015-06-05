package com.abuzdin.builder4j;

import org.junit.Test;

import static com.abuzdin.builder4j.TestBeans.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestBuilderTest {

    @Test
    public void shouldBuildNewInstance() {
        Person bean = TestBuilder.forBean(Person::new)
                .build();

        assertThat(bean, notNullValue());
    }

    @Test
    public void shouldBuildNewInstance2() {
        Person bean = TestBuilder.forBean(Person::new)
                .with(Person::setFirstName, "Hello")
                .with(Person::setChild,
                        TestBuilder.forBean(Child::new)
                                .with(Child::setAge, 1)
                                .build()
                ).build();

        assertThat(bean, notNullValue());
    }
}