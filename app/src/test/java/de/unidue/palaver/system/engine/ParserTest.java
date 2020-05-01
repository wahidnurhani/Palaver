package de.unidue.palaver.system.engine;

import org.junit.Test;

import de.unidue.palaver.system.model.CommunicatorResult;
import de.unidue.palaver.system.model.Friend;

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
        CommunicatorResult<Friend> communicatorResult = parser.getFriendParser(respose);
        assertEquals("marc",communicatorResult.getData().get(0).getUsername());
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void stringToDate() {
        Parser parser = new Parser();
        String date = "2016-02-12T17:01:44.623";
        System.out.println(parser.stringToDate(date).toGMTString());
    }

    @Test
    public void addFriendParser() {
        Parser parser = new Parser();
        String respose = "{\"MsgType\":1,\"Info\":\"Freunde aufgelistet\",\"Data\":[\"marc\"]}";
        CommunicatorResult<Friend> communicatorResult = parser.addContactReportParser(respose);
        System.out.println(communicatorResult.toString());
    }
}