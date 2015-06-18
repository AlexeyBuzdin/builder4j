builder4j
=========

Tired of writing, generating and maintaining builders for your Java Beans? Look no further!
Builder4J enchants your tests with a generic builder API.

### Usage
```
Person person = TestBuilder.forBean(Person::new)
                .with(Person::setFirstName, "Hello")
                .with(Person::setChild,
                        TestBuilder.forBean(Child::new)
                                .with(Child::setAge, 1)
                                .build()
                ).build();
```
