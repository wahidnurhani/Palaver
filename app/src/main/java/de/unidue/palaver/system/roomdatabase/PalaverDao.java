package de.unidue.palaver.system.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import de.unidue.palaver.system.model.Friend;


@Dao
public interface PalaverDao {

    @Insert
    void insert(Friend friend);

    @Delete
    void delete(Friend friend);
}
