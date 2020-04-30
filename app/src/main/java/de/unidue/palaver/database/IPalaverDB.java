package de.unidue.palaver.database;

import java.sql.SQLException;
import java.util.List;

import de.unidue.palaver.model.Chat;
import de.unidue.palaver.model.ChatItem;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.User;

public interface IPalaverDB {


    Chat getChat(Friend friend);

    public boolean insertContact(Friend friend);

    public boolean insertChatData(Friend friend, ChatItem chatItem);


    //public boolean deleteUserData();

    public boolean deleteContact(Friend friend);

    public boolean deleteChat(Friend friend);

    public boolean deleteAllChat()throws SQLException;

    public boolean deleteAllContact() throws SQLException;

    public boolean deleteAllDataOnDataBase();


    //querys

    //Update
    boolean updateIsReadValue(Friend friend);

    public List<Chat> getAllChat(User user);

    public List<Friend> getAllFriends();

    public List<ChatItem> getAllChatData(User user, String friend);

    public boolean checkContact(String friend);

    boolean updateDateTimeValue(Friend friend, ChatItem chatItem, String newDate);
}
