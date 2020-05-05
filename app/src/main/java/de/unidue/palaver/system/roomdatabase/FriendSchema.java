package de.unidue.palaver.system.roomdatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import de.unidue.palaver.system.resource.DBContract;

@Entity (tableName = "friends")
public class FriendSchema {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DBContract.TableFriend.COLUMN_FRIEND_NAME)
    private String username;


    public FriendSchema(String username) {
        this.username = username;
    }

    @NonNull
    public String getUsername() {
        return username;
    }
}
