package de.unidue.palaver.system.engine.communicator;

import org.junit.Test;

import de.unidue.palaver.system.engine.JSONBuilder;

import static org.junit.Assert.*;

public class JSONBuilderTest {

    @Test
    public void formatBodyUserDataToJSON() {
        JSONBuilder jsonBuilder = new JSONBuilder();
        assertEquals("{\"Username\":\"Test\",\"Password\":\"Pass\"}",
                jsonBuilder.formatBodyUserDataToJSON("Test", "Pass").toString());
    }
}