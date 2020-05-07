package de.unidue.palaver.system.roomdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import de.unidue.palaver.system.model.Chat;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;


@Dao
public interface PalaverDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Friend friend);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Message message);

    @Delete
    void delete(Friend friend);

    @Delete
    void delete(Message message);

    @Query("SELECT * FROM table_friend")
    List<Friend> loadAllFriend();

    @Transaction
    @Query("SELECT * FROM table_friend")
    List<Chat> loadAllChat();

    @Query("SELECT * From table_chat_data WHERE fk_friend = :friendName ")
    List<Message> loadChat(String friendName);

    @Update
    void updateMessage(Message message);

    @Update
    void updateMessages(Message... messages);

    @Query("SELECT * FROM table_friend WHERE friend_name = :friendUserName")
    Friend findUserByName(String friendUserName);

    @Query("DELETE FROM table_friend")
    int deleteFriend();

    @Query("DELETE FROM table_chat_data")
    int deleteAllChat();
}
