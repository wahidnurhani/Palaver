package de.unidue.palaver.system.database;

import java.sql.SQLException;
import java.util.List;

import de.unidue.palaver.system.MessageViewModel;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.Friend;

public interface IPalaverDB {


    MessageViewModel getChat(Friend friend);

    public boolean insertFriend(Friend friend);

    public boolean insertChatItem(Friend friend, Message message);

    public boolean deleteContact(Friend friend);

    public boolean deleteChat(Friend friend);

    public boolean deleteAllChat()throws SQLException;

    public boolean deleteAllContact() throws SQLException;

    public boolean deleteAllDataOnDataBase();


    boolean updateIsReadValue(Friend friend);

    public List<MessageViewModel> getAllChat();

    public List<Friend> getAllFriends();

    public List<Message> getAllChatData(Friend friend);

    public boolean checkContact(String friend);

    boolean updateDateTimeValue(Friend friend, Message message, String newDate);
}
