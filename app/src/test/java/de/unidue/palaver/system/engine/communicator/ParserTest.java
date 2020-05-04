package de.unidue.palaver.system.engine.communicator;

import org.json.JSONException;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import de.unidue.palaver.system.model.Message;
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
    public void stringToDate() throws ParseException {
        Parser parser = new Parser();
        String date = "2016-02-12T17:01:44.623";
        System.out.println(parser.stringToDateFromServer(date).toGMTString());
    }

    @Test
    public void addFriendParser() {
        Parser parser = new Parser();
        String respose = "{\"MsgType\":1,\"Info\":\"Freunde aufgelistet\",\"Data\":[\"marc\"]}";
        CommunicatorResult<Friend> communicatorResult = parser.addAndRemoveFriendReportParser(respose);
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void changePasswordParser() {
        Parser parser = new Parser();
        String newPassword= "Test";
        String respose = "{\"MsgType\":1,\"Info\":\"Passwort erfolgreich aktualisiert\", \"Data\":null}";
        CommunicatorResult<String> communicatorResult = parser.changePasswordResultParser(respose, newPassword);
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void pushTokenParser(){
        Parser parser = new Parser();
        String respose = "{\"MsgType\":1,\"Info\":\"PushToken erfolgreich aktualisiert\", \"Data\":null}";
        CommunicatorResult<String> communicatorResult = parser.pushTokenParser(respose);
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void sendMessageParser() {
        Parser parser = new Parser();
        String respose = "{\"MsgType\":1, \"Info\":\"Nachricht verschickt\", " +
                "\"Data\":{\"DateTime\":\"2016-02-12T17:01:44.6224075+01:00\"}}";
        CommunicatorResult<Date> communicatorResult = parser.sendMessageReport(respose);
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void getChatDataParser(){
        Parser parser = new Parser();
        String respose = "{\"MsgType\":1,\"Info\":\"Nachrichten abgerufen\"," +
                "\"Data\":[{ \"Sender\":\"marc\", \"Recipient\": \"stefan\", \"Mimetype\":\"text/plain\", " +
                "\"Data\":\"Blubber!\", \"DateTime\":\"2016-02-12T17:02:38.663\"}]}";
        CommunicatorResult<Message> communicatorResult = null;
        try {
            communicatorResult = parser.getChatDataParser(respose, "false", "stefan");
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        assert communicatorResult != null;
        assertEquals(1, communicatorResult.getResponseValue());
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void dateToString() throws ParseException {
        Parser parser = new Parser();
        Date date = new Date();
        System.out.println(date.toString());
        String dateString = parser.dateToString(date);
        System.out.println(dateString);
        Date date1 = parser.stringToDateFromDataBase(dateString);
        System.out.println(date1);


    }
}