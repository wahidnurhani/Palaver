package de.unidue.palaver.roomdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * FROM table_friend")
    LiveData<List<Friend>> getAllFriend();

    @Query("SELECT * FROM table_friend")
    List<Friend> getAllFriendList();


    @Query("SELECT * " +
            "FROM (select fk_friend, table_chat_data.data, date_time " +
            "from table_friend INNER JOIN table_chat_data " +
            "on fk_friend = friend_name order by date_time ASC) " +
            "GROUP By fk_friend ")
    LiveData<List<Chat>> getAllChat();

    @Query("SELECT * From table_chat_data WHERE sender = :friendName " +
            "OR recipient = :friendName ORDER BY date_time ASC" )
    LiveData<List<Message>> getMessages(String friendName);

    @Query("SELECT * From table_chat_data WHERE sender = :friendName " +
            "OR recipient = :friendName ORDER BY date_time ASC" )
    List<Message> getMessagesList(String friendName);

    @Update
    int updateMessage(Message message);

    @Update
    int updateMessages(Message... messages);

    @Query("SELECT * FROM table_friend WHERE friend_name = :friendUserName")
    Friend findUserByName(String friendUserName);

    @Query("DELETE FROM table_friend")
    int deleteAllFriend();

    @Query("DELETE FROM table_chat_data")
    int deleteAllChat();

    @Update
    int update(Friend friend);

    @Update
    int update(Message message);


}
