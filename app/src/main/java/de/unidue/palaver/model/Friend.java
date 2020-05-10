package de.unidue.palaver.model;

import androidx.annotation.NonNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import de.unidue.palaver.roomdatabase.DBContract;

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
        return this.username+"";
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @Override
    public int compareTo(Friend o) {
        return username.compareTo(o.getUsername());
    }

    @Override
    public String toString() {
        return "Friend{" +
                "username='" + username + '\'' +
                '}';
    }
}
