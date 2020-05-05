package de.unidue.palaver.system.model;

import androidx.annotation.NonNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import de.unidue.palaver.system.resource.DBContract;

@Entity(tableName = DBContract.TableFriend.TABLE_FFRIEND_NAME)
public class Friend implements Comparable<Friend>, Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DBContract.TableFriend.COLUMN_FRIEND_NAME)
    private String username;

    public Friend(String username) {
        this.username = username;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @Override
    public int compareTo(Friend o) {
        return username.compareTo(o.getUsername());
    }
}
