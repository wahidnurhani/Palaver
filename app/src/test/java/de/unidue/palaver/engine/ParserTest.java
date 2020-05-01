package de.unidue.palaver.engine;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void validateAndRegisterReportParser() {

        Parser parser = new Parser();
        String respose = "{\"MsgType\":1,\"Info\":\"Benutzer erfolgreich validiert\", \"Data\":null}";
        assertEquals("1", parser.validateAndRegisterReportParser(respose)[0]);
        assertNull(parser.validateAndRegisterReportParser(respose)[2]);
    }

    @Test
    public void getFriendParser() {
        Parser parser = new Parser();
        String respose = "{\"MsgType\":1,\"Info\":\"Freunde aufgelistet\",\"Data\":[\"marc\"]}";
        assertEquals("marc",parser.getFriendParser(respose)[0].getUsername());
    }

    @Test
    public void stringToDate() {
        Parser parser = new Parser();
        String date = "2016-02-12T17:01:44.623";
        System.out.println(parser.stringToDate(date).toGMTString());
    }
}