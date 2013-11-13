builder4j
=========

Tired of writing and maintaining builders for your Java Beans? Look no further! 
Builder4J enchants your test with generic reflective builders. 

#### Creating beans using field names
```
@Test
public void shouldSetStringField() {
    MyBean bean = TestBuilder.forBean(MyBean.class)
            .withField("stringField", "Hello World")
            .build();

    assertThat(bean, hasProperty("stringField", is("Hello World")));
}
```

#### Creating beans using getter methods on dynamic proxy
```
@InjectProxy
TestBean bean;

@Before
public void setUp() {
    Builder4JAnnotations.initProxies(this);
}

@Test
public void shouldSetStringField() {
    bean = TestBuilder.forBean(MyBean.class, bean)
            .with(bean.getStringField(), "Hello World")
            .build();

    assertThat(bean, hasProperty("stringField", is("Hello World")));
}
```
