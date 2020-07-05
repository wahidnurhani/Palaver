package de.unidue.palaver.roomdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import de.unidue.palaver.model.Chat;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;


@Dao
public interface PalaverDao{

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long insert(Friend friend);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Message message);

    @Delete
    int delete(Friend friend);

    @Delete
    int delete(Message message);

    @Query("SELECT * FROM table_friend ORDER BY friend_name ASC")
    LiveData<List<Friend>> getAllFriend();

    @Query("SELECT * FROM table_friend ORDER BY friend_name ASC")
    List<Friend> getAllFriendList();


    @Query("SELECT * " +
            "FROM (select fk_friend, table_chat_data.data, date_time " +
            "from table_friend INNER JOIN table_chat_data " +
            "on fk_friend = friend_name order by date_time ASC) " +
            "GROUP By fk_friend ORDER BY date_time DESC")
    LiveData<List<Chat>> getAllChat();

    @Query("SELECT * From table_chat_data WHERE sender = :friendName " +
            "OR recipient = :friendName ORDER BY date_time ASC" )
    LiveData<List<Message>> getMessages(String friendName);

    @Query("SELECT * From table_chat_data WHERE sender = :friendName " +
            "OR recipient = :friendName ORDER BY date_time ASC" )
    List<Message> getMessagesList(String friendName);

    @Query("DELETE FROM table_friend")
    void deleteAllFriend();

    @Query("DELETE FROM table_chat_data")
    void deleteAllChat();

    @Query("SELECT date_time FROM table_chat_data WHERE fk_friend = :friend " +
            "ORDER BY date_time DESC LIMIT 1")
    String getOffset(String friend);

    @Query("DELETE FROM table_chat_data WHERE fk_friend=:friend")
    void clearMessage(String friend);
}
