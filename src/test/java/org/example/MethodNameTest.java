package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StackInfoUtilTest {

    @Test
    void myMethodName() {
        assertEquals("myMethodName", StackInfoUtil.getCurrentMethodName());
    }

    @Test
    void myMethodName_inClasses() {
        class InnerClass {
            public final String innerName = StackInfoUtil.getCurrentMethodName();
        }
        assertEquals("<init>", new InnerClass().innerName);
    }

    @Test
    void myClassName() {
        assertEquals("org.example.StackInfoUtilTest", StackInfoUtil.getCurrentClassName());
    }

    @Test
    void myClassName_inClasses() {

        class InnerClass {
            public final String innerName = StackInfoUtil.getCurrentClassName();
        }
        assertEquals("org.example.StackInfoUtilTest$2InnerClass", new InnerClass().innerName);
    }
}
