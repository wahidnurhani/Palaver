package de.unidue.palaver.system.resource;

import android.provider.BaseColumns;

public class DBContract {

    public static final String DB_NAME = "palaver.db";
    public static final int DB_VERSION = 1;

    public DBContract(){}


    public static abstract class TableFriend implements BaseColumns{

        public static final String TABLE_FFRIEND_NAME = "table_friend";

        public static final String COLUMN_FRIEND_NAME = "friend_name";

        public static final String CREATE_TABLE_CONTACT = "CREATE TABLE "+ TABLE_FFRIEND_NAME +"("+ COLUMN_FRIEND_NAME +" TEXT PRIMARY KEY"+")" ;
        public static final String DELETE_TABLE_CONTACT = "DROP TABLE IF EXISTS " + TABLE_FFRIEND_NAME;
    }

    public static abstract class TableChatData implements BaseColumns {

        public static final String TABLE_CHAT_DATA_NAME = "table_chat_data";

        public static final String COLUMN_FKCHAT = "fk_chat";
        public static final String COLUMN_CHAT_SENDER = "sender";
        public static final String COLUMN_CHAT_RECIPIENT = "recipient";
        public static final String COLUMN_CHAT_MIMETYPE= "mimetype";
        public static final String COLUMN_CHAT_DATA= "data";
        public static final String COLUMN_CHAT_DATA_ISREAD="isread";
        public static final String COLUMN_CHAT_DATETIME= "date_time";

        public static final String CREATE_TABLE_CHAT_DATA =  "CREATE TABLE "+ TABLE_CHAT_DATA_NAME +"("+COLUMN_FKCHAT+" TEXT NOT NULL, "
                +COLUMN_CHAT_SENDER+" TEXT NOT NULL, "
                +COLUMN_CHAT_RECIPIENT+" TEXT NOT NULL, "
                +COLUMN_CHAT_MIMETYPE+" TEXT NOT NULL, "
                +COLUMN_CHAT_DATA+" TEXT NOT NULL, "
                +COLUMN_CHAT_DATETIME+" TEXT NOT NULL,"
                +COLUMN_CHAT_DATA_ISREAD+" TEXT NOT NULL,"
                +" FOREIGN KEY "+"("+ COLUMN_FKCHAT+")"+" REFERENCES "+ TableFriend.TABLE_FFRIEND_NAME +" ("+ TableFriend.COLUMN_FRIEND_NAME +")"+","
                +" PRIMARY KEY"+"("+COLUMN_FKCHAT+","+COLUMN_CHAT_SENDER+","+COLUMN_CHAT_DATA+","+COLUMN_CHAT_DATETIME+"))";

        public static final String DELETE_TABLE_CONTACT = "DROP TABLE IF EXISTS " + TABLE_CHAT_DATA_NAME;
    }

    public static abstract class Query implements BaseColumns{

        public static final String checkContactExistenceBaseQuery =
                "SELECT EXISTS (SELECT 1 FROM "+ TableFriend.COLUMN_FRIEND_NAME +" where "+ TableFriend.COLUMN_FRIEND_NAME +"=";

        public static final String getLastTwoDateTimeQueryBase=
                "SELECT "+ TableChatData.COLUMN_CHAT_DATETIME+" FROM "+TableChatData.TABLE_CHAT_DATA_NAME+
                        " WHERE "+TableChatData.COLUMN_FKCHAT+"=" ;
    }
}