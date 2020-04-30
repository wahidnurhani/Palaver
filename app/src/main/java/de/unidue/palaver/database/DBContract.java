package de.unidue.palaver.database;

import android.provider.BaseColumns;

class DBContract {

    static final String DB_NAME = "palaver.db";
    static final int DB_VERSION = 1;

    private DBContract(){}


    static abstract class TableContact implements BaseColumns{

        static final String TABLE_FFRIEND_NAME = "table_friend";

        static final String COLUMN_FRIEND_NAME = "friend_name";

        static final String CREATE_TABLE_CONTACT = "CREATE TABLE "+ TABLE_FFRIEND_NAME +"("+ COLUMN_FRIEND_NAME +" TEXT PRIMARY KEY"+")" ;
        static final String DELETE_TABLE_CONTACT = "DROP TABLE IF EXISTS " + TABLE_FFRIEND_NAME;
    }

    static abstract class TableChatData implements BaseColumns {

        static final String TABLE_CHAT_DATA_NAME = "table_chat_data";

        static final String COLUMN_FKCHAT = "fk_chat";
        static final String COLUMN_CHAT_SENDER = "sender";
        static final String COLUMN_CHAT_RECIPIENT = "recipient";
        static final String COLUMN_CHAT_MIMETYPE= "mimetype";
        static final String COLUMN_CHAT_DATA= "data";
        static final String COLUMN_CHAT_DATA_ISREAD="isread";
        static final String COLUMN_CHAT_DATETIME= "date_time";

        static final String CREATE_TABLE_CHAT_DATA =  "CREATE TABLE "+ TABLE_CHAT_DATA_NAME +"("+COLUMN_FKCHAT+" TEXT NOT NULL, "
                +COLUMN_CHAT_SENDER+" TEXT NOT NULL, "
                +COLUMN_CHAT_RECIPIENT+" TEXT NOT NULL, "
                +COLUMN_CHAT_MIMETYPE+" TEXT NOT NULL, "
                +COLUMN_CHAT_DATA+" TEXT NOT NULL, "
                +COLUMN_CHAT_DATETIME+" TEXT NOT NULL,"
                +COLUMN_CHAT_DATA_ISREAD+" TEXT NOT NULL,"
                +" FOREIGN KEY "+"("+ COLUMN_FKCHAT+")"+" REFERENCES "+ TableContact.TABLE_FFRIEND_NAME +" ("+TableContact.COLUMN_FRIEND_NAME +")"+","
                +" PRIMARY KEY"+"("+COLUMN_FKCHAT+","+COLUMN_CHAT_SENDER+","+COLUMN_CHAT_DATA+","+COLUMN_CHAT_DATETIME+"))";

        static final String DELETE_TABLE_CONTACT = "DROP TABLE IF EXISTS " + TABLE_CHAT_DATA_NAME;
    }

    public static abstract class Query implements BaseColumns{

        static final String checkContactExistenceBaseQuery =
                "SELECT EXISTS (SELECT 1 FROM "+TableContact.COLUMN_FRIEND_NAME +" where "+TableContact.COLUMN_FRIEND_NAME +"=";

        public static final String getLastTwoDateTimeQueryBase=
                "SELECT "+ TableChatData.COLUMN_CHAT_DATETIME+" FROM "+TableChatData.TABLE_CHAT_DATA_NAME+
                        " WHERE "+TableChatData.COLUMN_FKCHAT+"=" ;
    }
}