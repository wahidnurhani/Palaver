package de.unidue.palaver.system.resource;

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
        public static final String FRIEND_USERNAME = "INTENT_FRIEND_USERNAME";
        public static final String BROADCAST_FRIENDADDED_RESULT = "INTENT_ADDFRIEND_RESULT";
    }

    public static class IntentAction {
        public static final String BROADCAST_FRIENDADDED = "friendadded_broadcast";
        public static final String BROADCAST_AUTHENTIFICATED = "authentificated_broadcast";
        public static final String BROADCAST_ALL_FRIENDS_FETCHED = "friendfetched_broadcast";
        public static final String BROADCAST_USER_REGISTERED = "registered_broadcast";
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
    }

    public static class System {
        public static final String BASE_URL = "http://palaver.se.paluno.uni-due.de";
    }

    public static class APICmd {
        public static final String REGISTER = "/api/user/register";
        public static final String VALIDATE = "/api/user/validate";
        public static final String GET_ALL_FRIENDS = "/api/friends/get";
        public static final String ADD_FRIEND = "/api/friends/add";
        public static final String CHANGE_PASSWORD = "/api/user/password";
    }
}
