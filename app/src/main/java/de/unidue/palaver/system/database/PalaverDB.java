package de.unidue.palaver.system.database;

import android.annotation.SuppressLint;
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

import de.unidue.palaver.system.MessageManager;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.resource.MessageType;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.resource.DBContract;

public class PalaverDB implements IPalaverDB{
    private static final String TAG = PalaverDB.class.getSimpleName();
    private Context contex;
    private SQLiteDatabase sqLiteDatabase;
    @SuppressLint("StaticFieldLeak")
    private static PalaverDB palaverDBinstance;

    public static PalaverDB getPalaverDBInstace(Context context){
        if (palaverDBinstance == null && context!= null){
            palaverDBinstance = new PalaverDB(context);
        }
        return palaverDBinstance;
    }

    private PalaverDB(Context context) {
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
    public MessageManager getChat(Friend friend) {
        return null;
    }

    @Override
    public synchronized boolean insertFriend(Friend friend) {
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
    public synchronized boolean insertChatItem(Friend friend, Message message) {
        boolean returnValue= false;
        String contactString = friend.getUsername();
        String sender = message.getSender();
        String recipient = message.getRecipient();
        String mimeType = message.getMimeType();
        String data = message.getMessage();
        String isRead = Boolean.toString(message.getIsReadStatus());
        String dateTime = message.getDateToString();
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
        boolean returnValue2 = false;
        if(sqLiteDatabase.delete(DBContract.TableChatData.TABLE_CHAT_DATA_NAME, DBContract.TableChatData.COLUMN_FKCHAT+"=?", new String[]{friend.getUsername()})>0){
            Log.i(TAG, "delete chatData success from :"+friend.getUsername());
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
        boolean returnValue;
        if(sqLiteDatabase.delete(
                DBContract.TableContact.TABLE_FFRIEND_NAME,
                null, null)>0){
            Log.i(TAG, "delete All Contact success :");
        }
        returnValue= true;
        Log.i(TAG, "delete all data success ? :"+ returnValue);
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
    public List<MessageManager> getAllChat() {
        List<Friend> friendsList = getAllFriends();
        List<MessageManager> messageManagerList = new ArrayList<>();

        for(Friend friend:friendsList){
            List<Message> messages = getAllChatData(friend);
            if(messages.size()>0){
                MessageManager tmp= new MessageManager(friend);
                tmp.setChatItems(messages);
                messageManagerList.add(tmp);
            }
        }
        Collections.sort(messageManagerList);
        return messageManagerList;
    }

    @Override
    public List<Friend> getAllFriends() {
        List<Friend> friendList = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.query(
                DBContract.TableContact.TABLE_FFRIEND_NAME,
                new String[]{DBContract.TableContact.COLUMN_FRIEND_NAME},null, null,
                null,null,DBContract.TableContact.COLUMN_FRIEND_NAME+" ASC");

        if (cursor!=null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                Friend friend = new Friend(cursor.getString(0));
                friendList.add(friend);
            }
        }
        return friendList;
    }

    @Override
    public List<Message> getAllChatData(Friend friend) {
        List<Message> messageList= new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.query(
                DBContract.TableChatData.TABLE_CHAT_DATA_NAME,
                new String[]{DBContract.TableChatData.COLUMN_FKCHAT,
                        DBContract.TableChatData.COLUMN_CHAT_SENDER,
                        DBContract.TableChatData.COLUMN_CHAT_RECIPIENT,
                        DBContract.TableChatData.COLUMN_CHAT_MIMETYPE,
                        DBContract.TableChatData.COLUMN_CHAT_DATA,
                        DBContract.TableChatData.COLUMN_CHAT_DATA_ISREAD,
                        DBContract.TableChatData.COLUMN_CHAT_DATETIME},
                DBContract.TableChatData.COLUMN_FKCHAT+"=?", new String[]{friend.getUsername()},
                null,null, DBContract.TableChatData.COLUMN_CHAT_DATETIME);

        if (cursor!=null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                MessageType messageType;
                User user = SessionManager.getSessionManagerInstance(contex).getUser();
                if(cursor.getString(1).equals(user.getUserData().getUserName())){
                    messageType = MessageType.OUT;
                }else {
                    messageType = MessageType.INCOMMING;
                }
                Message message = new Message(cursor.getString(1),
                        cursor.getString(2),
                        messageType,
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
                messageList.add(message);
            }
            Log.i(TAG, "get All ChatData from: "+friend);
        }
        return messageList;
    }

    @Override
    public boolean checkContact(String friend) {
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(
                DBContract.Query.checkContactExistenceBaseQuery+friend+")",null);
        if (cursor.getCount() == 1){
            return cursor.getShort(1) == 1;
        }
        return false;
    }

    @Override
    public synchronized boolean updateDateTimeValue(Friend friend, Message message, String newDate) {
        return false;
    }
}
