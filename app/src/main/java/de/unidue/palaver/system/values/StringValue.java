package de.unidue.palaver.system.values;

import android.content.Intent;

public class StringValue {

    public static class ErrorMessage {
        //Select
        public static final String NO_INTERNET = "No Internet Connection";

        public static final String PASSWORD_DON_T_MATCH_EACH_OTHER = "Password don't match each other";
        public static final String PLEASE_INPUT_VALID_USERNAME_FORMAT = "Please input valid username-format";
        public static final String USERNAME_PASSWORD_BLANK = "The Username and Password cannot be blank";
        public static final String USERNAME_BLANK = "The Username cannot be blank";
        public static final String ADD_OWN_ACCOUNT = "you can't add your own account";
    }

    public static class TextAndLabel {
        public static final String CLOSE = "Close";
        public static final String SELECT_FRIEND = "Select Friend";
    }

    public static class IntentKeyName {
        public static final String FRIEND = "INTENT_FRIEND";
        public static final String BROADCAST_FRIENDADDED_MESSAGE_RESULT = "INTENT_ADDFRIEND_RESULT";
        public static final String MESSAGE = "INTENT_MESSAGE";
        public static final String USERNAME = "USERNAME";
        public static final String PASSWORD = "PASSWORD";
    }

    public static class IntentAction {
        public static final String BROADCAST_FRIENDADDED = "friendadded_broadcast";
        public static final String BROADCAST_AUTHENTIFICATED = "authentificated_broadcast";
        public static final String BROADCAST_ALL_FRIENDS_FETCHED = "friendfetched_broadcast";
        public static final String BROADCAST_USER_REGISTERED = "registered_broadcast";
        public static final String BROADCAST_ALL_CHAT_FETCHED = "allChatfetched_broadcast";
        public static final String BROADCAST_LOGINRESULT = "loginResult_broadcast";
        public static final String BROADCAST_STARTLOGIN = "loginStart_BroadCast";
    }

    public static class LogMessage {
        public static final String SERVICE_DESTROYED = "service destroyed";
    }

    public static class JSONKeyName {
        public static final String MSG_TYPE = "MsgType";
        public static final String INFO = "Info";
        public static final String DATA = "Data";
        public static final String USERNAME = "Username";
        public static final String PASSWORD = "Password";
        public static final String FRIEND = "Friend";
        public static final String NEWPASSWORD = "NewPassword";
        public static final String PUSH_TOKEN = "PushToken";
        public static final String RECIPIENT = "Recipient";
        public static final String MIME_TYPE = "Mimetype";
        public static final String MIME_TYPE_VALUE = "text/plain";
        public static final String DATE_TIME = "DateTime";
        public static final String SENDER = "Sender";
    }

    public static class System {
        public static final String BASE_URL = "http://palaver.se.paluno.uni-due.de";
    }

    public static class APICmd {
        public static final String REGISTER = "/api/user/register";
        public static final String VALIDATE = "/api/user/validate";
        public static final String GET_ALL_FRIENDS = "/api/friends/get";
        public static final String ADD_FRIEND = "/api/friends/add";
        public static final String REMOVE_FRIEND = "/api/friends/remove";
        public static final String CHANGE_PASSWORD = "/api/user/password";
        public static final String PUSHTOKEN = "/api/user/pushtoken";
        public static final String SEND_MESSAGE = "/api/message/send";
        public static final String GET_MESSAGE = "/api/message/get";

        public static final String BASE_URL = "http://palaver.se.paluno.uni-due.de";
    }
}
