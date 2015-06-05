package com.abuzdin.builder4j;

public class TestBeans {

    public static class Person {

        private String stringField;
        private int intField;
        private Child child;

        public String getStringField() {
            return stringField;
        }

        public void setFirstName(String stringField) {
            this.stringField = stringField;
        }

        public int getIntField() {
            return intField;
        }

        public void setIntField(int intField) {
            this.intField = intField;
        }

        public Child getChild() {
            return child;
        }

        public void setChild(Child child) {
            this.child = child;
        }

        public int getNonExistingField(){
            return  1;
        }

        public int nonGetterMethod(){
            return  1;
        }

        public int getFieldWithOtherName(){
            return  intField;
        }

        public void setFieldWithOtherName(int i){
            intField = i;
        }
    }

    public static class Child {

        private int intField;

        public int getIntField() {
            return intField;
        }

        public void setAge(int intField) {
            this.intField = intField;
        }
    }

    public static class NoDefaultConstructorBean {

        private NoDefaultConstructorBean() {}
    }

    public static class MultipleConstructorBean {

        public MultipleConstructorBean() {}

        public MultipleConstructorBean(int i) {}
    }
}
