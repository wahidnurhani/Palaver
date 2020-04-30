package de.unidue.palaver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unidue.palaver.model.Chat;
import de.unidue.palaver.model.ChatItem;
import de.unidue.palaver.model.ChatItemType;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.User;

public class PalaverDBManager implements IPalaverDB{
    private static final String TAG = PalaverDBManager.class.getSimpleName();
    private Context contex;
    private SQLiteDatabase sqLiteDatabase;

    public PalaverDBManager(Context context) {
        this.contex = context;

        sqLiteDatabase = new SQLiteOpenHelper(this.contex, DBContract.DB_NAME,
                null, DBContract.DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(DBContract.TableContact.CREATE_TABLE_CONTACT);
                db.execSQL(DBContract.TableChatData.CREATE_TABLE_CHAT_DATA);
            }

            @Override
            public void onConfigure(SQLiteDatabase db) {
                super.onConfigure(db);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    db.setForeignKeyConstraintsEnabled(true);
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL(DBContract.TableContact.DELETE_TABLE_CONTACT);
                db.execSQL(DBContract.TableChatData.DELETE_TABLE_CONTACT);

                onCreate(db);

            }
        }.getWritableDatabase();
    }

    @Override
    public Chat getChat(Friend friend) {
        return null;
    }

    @Override
    public synchronized boolean insertContact(Friend friend) {
        boolean returnValue=false;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.TableContact.COLUMN_FRIEND_NAME, friend.getUsername());

        if(sqLiteDatabase.insert(DBContract.TableContact.TABLE_FFRIEND_NAME,null, contentValues)>0){
            returnValue= true;
            Log.i(TAG, "insert contact : "+ true);
        }
        return returnValue;
    }

    @Override
    public synchronized boolean insertChatData(Friend friend, ChatItem chatItem) {
        boolean returnValue= false;
        String contactString = friend.getUsername();
        String sender = chatItem.getSender();
        String recipient = chatItem.getRecipient();
        String mimeType = chatItem.getMimeType();
        String data = chatItem.getMessage();
        String isRead = Boolean.toString(chatItem.getIsReadStatus());
        String dateTime = chatItem.getDate().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.TableChatData.COLUMN_FKCHAT, contactString);
        contentValues.put(DBContract.TableChatData.COLUMN_CHAT_SENDER, sender);
        contentValues.put(DBContract.TableChatData.COLUMN_CHAT_RECIPIENT, recipient);
        contentValues.put(DBContract.TableChatData.COLUMN_CHAT_MIMETYPE, mimeType);
        contentValues.put(DBContract.TableChatData.COLUMN_CHAT_DATA, data);
        contentValues.put(DBContract.TableChatData.COLUMN_CHAT_DATA_ISREAD, isRead);
        contentValues.put(DBContract.TableChatData.COLUMN_CHAT_DATETIME,dateTime);
        if(sqLiteDatabase.insert(DBContract.TableChatData.TABLE_CHAT_DATA_NAME, null, contentValues)>0){
            returnValue=true;
            Log.i(TAG, "insert chat data : "+ true);
        }
        return returnValue;
    }

    @Override
    public synchronized boolean deleteContact(Friend friend) {
        boolean returnValue;
        boolean returnValue1 = false;
        boolean returnValue2 = false;
        if(sqLiteDatabase.delete(DBContract.TableChatData.TABLE_CHAT_DATA_NAME, DBContract.TableChatData.COLUMN_FKCHAT+"=?", new String[]{friend.getUsername()})>0){
            Log.i(TAG, "delete chatData success from :"+friend.getUsername());
            returnValue1 = true;
        }
        if(sqLiteDatabase.delete(DBContract.TableContact.TABLE_FFRIEND_NAME, DBContract.TableContact.COLUMN_FRIEND_NAME+"=?", new String[]{friend.getUsername()})>0){
            Log.i(TAG, "delete contact success from :"+friend.getUsername());
            returnValue2 = true;
        }
        returnValue = returnValue2;
        return returnValue;
    }

    @Override
    public synchronized boolean deleteChat(Friend friend) {
        boolean returnValue=false;
        if(sqLiteDatabase.delete(DBContract.TableChatData.TABLE_CHAT_DATA_NAME, DBContract.TableChatData.COLUMN_FKCHAT+"=?", new String[]{friend.getUsername()})>0){
            Log.i(TAG, "delete chatData success from :"+friend.getUsername());
            returnValue = true;
        }
        return returnValue;
    }

    @Override
    public synchronized boolean deleteAllChat() {
        boolean returnValue=false;
        if(sqLiteDatabase.delete(DBContract.TableChatData.TABLE_CHAT_DATA_NAME, null, null)>0){
            Log.i(TAG, "delete All chatData success ");
            returnValue = true;
        }
        return returnValue;
    }

    @Override
    public synchronized boolean deleteAllContact() {
        boolean returnValue = false;
        if(sqLiteDatabase.delete(
                DBContract.TableContact.TABLE_FFRIEND_NAME,
                null, null)>0){
            Log.i(TAG, "delete All Contact success :");
        }
        returnValue= true;
        Log.i(TAG, "delete all data success ? :"+ Boolean.toString(returnValue));
        return returnValue;
    }

    @Override
    public synchronized boolean deleteAllDataOnDataBase() {
        boolean returnValue = false;
        if(sqLiteDatabase.delete(
                DBContract.TableChatData.TABLE_CHAT_DATA_NAME,
                null, null)>0){
            Log.i(TAG, "delete All ChatData success ");
        }
        if(sqLiteDatabase.delete(
                DBContract.TableContact.TABLE_FFRIEND_NAME,
                null, null)>0){
            Log.i(TAG, "delete All Contact success :");
            returnValue= true;
        }
        Log.i(TAG, "delete all data success ? :"+ returnValue);
        return returnValue;
    }

    @Override
    public synchronized boolean updateIsReadValue(Friend friend) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.TableChatData.COLUMN_CHAT_DATA_ISREAD, "true");

        if(sqLiteDatabase.update(DBContract.TableChatData.TABLE_CHAT_DATA_NAME,
                contentValues,DBContract.TableChatData.COLUMN_FKCHAT+"=?",
                new String[]{friend.getUsername()})>0){
            Log.i(TAG, "change isRead into true : is true");
            return true;
        }
        return false;
    }

    @Override
    public synchronized List<Chat> getAllChat( User user ) {
        List<Chat> chatList = new ArrayList<>();
        List<Friend> friendsList = getAllFriends();

        for(Friend friend:friendsList){
            List<ChatItem>chatItems= getAllChatData(user, friend.getUsername());
            if(chatItems.size()>0){

                Chat tmp= new Chat(friend);
                tmp.setChatItems(chatItems);
                chatList.add(tmp);
            }
        }
        Collections.sort(chatList);
        return chatList;
    }

    @Override
    public synchronized List<Friend> getAllFriends() {
        List<Friend> contactList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(
                DBContract.TableContact.TABLE_FFRIEND_NAME,
                new String[]{DBContract.TableContact.COLUMN_FRIEND_NAME},null, null,
                null,null,DBContract.TableContact.COLUMN_FRIEND_NAME+" ASC");

        if (cursor!=null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                Friend friend = new Friend(cursor.getString(0));
                contactList.add(friend);
            }
        }
        return contactList;
    }

    @Override
    public synchronized List<ChatItem> getAllChatData(User user, String friend) {
        List<ChatItem> chatDataList= new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(
                DBContract.TableChatData.TABLE_CHAT_DATA_NAME,
                new String[]{DBContract.TableChatData.COLUMN_FKCHAT,
                        DBContract.TableChatData.COLUMN_CHAT_SENDER,
                        DBContract.TableChatData.COLUMN_CHAT_RECIPIENT,
                        DBContract.TableChatData.COLUMN_CHAT_MIMETYPE,
                        DBContract.TableChatData.COLUMN_CHAT_DATA,
                        DBContract.TableChatData.COLUMN_CHAT_DATA_ISREAD,
                        DBContract.TableChatData.COLUMN_CHAT_DATETIME},
                DBContract.TableChatData.COLUMN_FKCHAT+"=?", new String[]{friend},
                null,null, DBContract.TableChatData.COLUMN_CHAT_DATETIME);

        if (cursor!=null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                ChatItemType chatItemType;
                if(cursor.getString(1).equals(user.getUserData().getUserName())){
                    chatItemType = ChatItemType.OUT;
                }else {
                    chatItemType = ChatItemType.INCOMMING;
                }
                ChatItem chatItem = new ChatItem(cursor.getString(1),
                        cursor.getString(2),
                        chatItemType,
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
                chatDataList.add(chatItem);
            }
            Log.i(TAG, "get All ChatData from: "+friend);
        }
        return chatDataList;
    }

    @Override
    public synchronized boolean checkContact(String friend) {
        Cursor cursor = sqLiteDatabase.rawQuery(
                DBContract.Query.checkContactExistenceBaseQuery+friend+")",null);
        if (cursor.getCount() == 1){
            if(cursor.getShort(1)==1){
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean updateDateTimeValue(Friend friend, ChatItem chatItem, String newDate) {
        return false;
    }
}
