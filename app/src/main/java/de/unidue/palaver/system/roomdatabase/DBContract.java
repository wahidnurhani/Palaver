package de.unidue.palaver.system.roomdatabase;

import android.provider.BaseColumns;

public class DBContract {

    public static final String DB_NAME = "palaver.db";
    public static final int DB_VERSION = 2;

    public DBContract(){}


    public static abstract class TableFriend implements BaseColumns{

        public static final String TABLE_FFRIEND_NAME = "table_friend";
        public static final String COLUMN_FRIEND_NAME = "friend_name";
    }

    public static abstract class TableMessage implements BaseColumns {

        public static final String TABLE_MESSAGE_NAME = "table_chat_data";

        public static final String COLUMN_FKFRIEND = "fk_friend";
        public static final String COLUMN_CHAT_SENDER = "sender";
        public static final String COLUMN_CHAT_RECIPIENT = "recipient";
        public static final String COLUMN_CHAT_MIMETYPE= "mimetype";
        public static final String COLUMN_MESSAGE_TYPE = "type";
        public static final String COLUMN_CHAT_DATA= "data";
        public static final String COLUMN_CHAT_DATA_ISREAD="isread";
        public static final String COLUMN_CHAT_DATETIME= "date_time";

    }
}