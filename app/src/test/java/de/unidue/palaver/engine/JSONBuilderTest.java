package de.unidue.palaver.engine;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONBuilderTest {

    @Test
    public void formatBodyUserDataToJSON() {
        JSONBuilder jsonBuilder = new JSONBuilder();
        System.out.println(jsonBuilder.formatBodyUserDataToJSON("Test", "Pass").toString());
    }
}