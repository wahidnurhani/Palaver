package de.unidue.palaver.system.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unidue.palaver.system.MessageViewModel;
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
                db.execSQL(DBContract.TableFriend.CREATE_TABLE_CONTACT);
                db.execSQL(DBContract.TableMessage.CREATE_TABLE_CHAT_DATA);
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
                db.execSQL(DBContract.TableFriend.DELETE_TABLE_CONTACT);
                db.execSQL(DBContract.TableMessage.DELETE_TABLE_CONTACT);

                onCreate(db);
            }
        }.getWritableDatabase();
    }

    @Override
    public MessageViewModel getChat(Friend friend) {
        return null;
    }

    @Override
    public synchronized boolean insertFriend(Friend friend) {
        boolean returnValue=false;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.TableFriend.COLUMN_FRIEND_NAME, friend.getUsername());

        if(sqLiteDatabase.insert(DBContract.TableFriend.TABLE_FFRIEND_NAME,null, contentValues)>0){
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
        contentValues.put(DBContract.TableMessage.COLUMN_FKCHAT, contactString);
        contentValues.put(DBContract.TableMessage.COLUMN_CHAT_SENDER, sender);
        contentValues.put(DBContract.TableMessage.COLUMN_CHAT_RECIPIENT, recipient);
        contentValues.put(DBContract.TableMessage.COLUMN_CHAT_MIMETYPE, mimeType);
        contentValues.put(DBContract.TableMessage.COLUMN_CHAT_DATA, data);
        contentValues.put(DBContract.TableMessage.COLUMN_CHAT_DATA_ISREAD, isRead);
        contentValues.put(DBContract.TableMessage.COLUMN_CHAT_DATETIME,dateTime);
        if(sqLiteDatabase.insert(DBContract.TableMessage.TABLE_MESSAGE_NAME, null, contentValues)>0){
            returnValue=true;
            Log.i(TAG, "insert chat data : "+ true);
        }
        return returnValue;
    }

    @Override
    public synchronized boolean deleteContact(Friend friend) {
        boolean returnValue;
        boolean returnValue2 = false;
        if(sqLiteDatabase.delete(DBContract.TableMessage.TABLE_MESSAGE_NAME, DBContract.TableMessage.COLUMN_FKCHAT+"=?", new String[]{friend.getUsername()})>0){
            Log.i(TAG, "delete chatData success from :"+friend.getUsername());
        }
        if(sqLiteDatabase.delete(DBContract.TableFriend.TABLE_FFRIEND_NAME, DBContract.TableFriend.COLUMN_FRIEND_NAME+"=?", new String[]{friend.getUsername()})>0){
            Log.i(TAG, "delete contact success from :"+friend.getUsername());
            returnValue2 = true;
        }
        returnValue = returnValue2;
        return returnValue;
    }

    @Override
    public synchronized boolean deleteChat(Friend friend) {
        boolean returnValue=false;
        if(sqLiteDatabase.delete(DBContract.TableMessage.TABLE_MESSAGE_NAME, DBContract.TableMessage.COLUMN_FKCHAT+"=?", new String[]{friend.getUsername()})>0){
            Log.i(TAG, "delete chatData success from :"+friend.getUsername());
            returnValue = true;
        }
        return returnValue;
    }

    @Override
    public synchronized boolean deleteAllChat() {
        boolean returnValue=false;
        if(sqLiteDatabase.delete(DBContract.TableMessage.TABLE_MESSAGE_NAME, null, null)>0){
            Log.i(TAG, "delete All chatData success ");
            returnValue = true;
        }
        return returnValue;
    }

    @Override
    public synchronized boolean deleteAllContact() {
        boolean returnValue;
        if(sqLiteDatabase.delete(
                DBContract.TableFriend.TABLE_FFRIEND_NAME,
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
                DBContract.TableMessage.TABLE_MESSAGE_NAME,
                null, null)>0){
            Log.i(TAG, "delete All ChatData success ");
        }
        if(sqLiteDatabase.delete(
                DBContract.TableFriend.TABLE_FFRIEND_NAME,
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
        contentValues.put(DBContract.TableMessage.COLUMN_CHAT_DATA_ISREAD, "true");

        if(sqLiteDatabase.update(DBContract.TableMessage.TABLE_MESSAGE_NAME,
                contentValues, DBContract.TableMessage.COLUMN_FKCHAT+"=?",
                new String[]{friend.getUsername()})>0){
            Log.i(TAG, "change isRead into true : is true");
            return true;
        }
        return false;
    }

    @Override
    public List<MessageViewModel> getAllChat() {
        List<Friend> friendsList = getAllFriends();
        List<MessageViewModel> messageViewModelList = new ArrayList<>();

        for(Friend friend:friendsList){
            List<Message> messages = getAllChatData(friend);
            if(messages.size()>0){
                //MessageViewModel tmp= new MessageViewModel(friend);
                //tmp.setChatItems(messages);
                //messageViewModelList.add(tmp);
            }
        }
        Collections.sort(messageViewModelList);
        return messageViewModelList;
    }

    @Override
    public List<Friend> getAllFriends() {
        List<Friend> friendList = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.query(
                DBContract.TableFriend.TABLE_FFRIEND_NAME,
                new String[]{DBContract.TableFriend.COLUMN_FRIEND_NAME},null, null,
                null,null, DBContract.TableFriend.COLUMN_FRIEND_NAME+" ASC");

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
                DBContract.TableMessage.TABLE_MESSAGE_NAME,
                new String[]{DBContract.TableMessage.COLUMN_FKCHAT,
                        DBContract.TableMessage.COLUMN_CHAT_SENDER,
                        DBContract.TableMessage.COLUMN_CHAT_RECIPIENT,
                        DBContract.TableMessage.COLUMN_CHAT_MIMETYPE,
                        DBContract.TableMessage.COLUMN_CHAT_DATA,
                        DBContract.TableMessage.COLUMN_CHAT_DATA_ISREAD,
                        DBContract.TableMessage.COLUMN_CHAT_DATETIME},
                DBContract.TableMessage.COLUMN_FKCHAT+"=?", new String[]{friend.getUsername()},
                null,null, DBContract.TableMessage.COLUMN_CHAT_DATETIME);

        if (cursor!=null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                MessageType messageType;
                User user = SessionManager.getSessionManagerInstance(contex).getUser();
                if(cursor.getString(1).equals(user.getUserData().getUserName())){
                    messageType = MessageType.OUT;
                }else {
                    messageType = MessageType.INCOMMING;
                }
                Message message = null;
                try {
                    message = new Message(cursor.getString(1),
                            cursor.getString(2),
                            messageType,
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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

    public void deleteMessage(Message message) {
        //TODO
    }
}
