package com.abuzdin.builder4j;

public class TestBeans {

    public static class TestBean {

        private String stringField;
        private int intField;
        private ChildBean childBean;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public int getIntField() {
            return intField;
        }

        public void setIntField(int intField) {
            this.intField = intField;
        }

        public ChildBean getChildBean() {
            return childBean;
        }

        public void setChildBean(ChildBean childBean) {
            this.childBean = childBean;
        }

        public int getNonExistingField(){
            return  1;
        }
    }

    public static class ChildBean {

        private int intField;

        public int getIntField() {
            return intField;
        }

        public void setIntField(int intField) {
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
