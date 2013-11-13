builder4j
=========

Tired of writing and maintaining builders for your Java Beans? Look no further! 
Builder4J enchants your test with generic reflective builders. 

##### Two ways of creating beans with builder4j

#### Using field names
<code>
    @Test
    public void shouldSetStringFieldWithSetter() {
        TestBean bean = TestBuilder.forBean(TestBean.class)
                .with("stringField", "Hello World")
                .build();

        assertThat(bean, hasProperty("stringField", is("Hello World")));
    }
</code>

#### Using getter methods on dynamic proxy
<code>
public class TestBuilderAnnotationTest {

    @InjectProxy
    TestBean bean;

    @Before
    public void setUp() {
        Builder4JAnnotations.initProxies(this);
    }

    @Test
    public void shouldSetStringField() {
        bean = TestBuilder.forBean(TestBean.class, bean)
                .with(bean.getStringField(), "Hello World")
                .build();

        assertThat(bean, hasProperty("stringField", is("Hello World")));
    }
}
</code>
