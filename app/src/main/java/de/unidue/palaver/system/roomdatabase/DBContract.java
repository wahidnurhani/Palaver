package de.unidue.palaver.system.roomdatabase;

import android.provider.BaseColumns;

public class DBContract {

    static final String DB_NAME = "palaver.db";
    static final int DB_VERSION = 1;

    public DBContract(){}

    public static abstract class TableFriend implements BaseColumns{

        public static final String TABLE_FFRIEND_NAME = "table_friend";

        public static final String COLUMN_FRIEND_NAME = "friend_name";
    }

    public static abstract class TableMessage implements BaseColumns {

        public static final String TABLE_MESSAGE_NAME = "table_chat_data";

        public static final String COLUMN_MESSAGE_SENDER = "sender";
        public static final String COLUMN_MESSAGE_RECIPIENT = "recipient";
        public static final String COLUMN_MESSAGE_MIMETYPE = "mimetype";
        public static final String COLUMN_MESSAGE_DATA = "data";
        public static final String COLUMN_MESSAGE_ISREAD ="isread";
        public static final String COLUMN_MESSAGE_DATETIME = "date_time";

    }
}