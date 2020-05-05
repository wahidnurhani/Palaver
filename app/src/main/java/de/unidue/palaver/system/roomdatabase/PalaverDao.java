package de.unidue.palaver.system.roomdatabase;

import androidx.room.Dao;
import androidx.room.Insert;


@Dao
public interface PalaverDao {

    @Insert
    void insert(FriendSchema friend);
}
