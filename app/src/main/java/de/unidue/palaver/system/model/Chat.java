package de.unidue.palaver.system.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import de.unidue.palaver.system.resource.DBContract;

public class Chat {

    @Embedded public Friend friend;
    @Relation(
            parentColumn =DBContract.TableFriend.COLUMN_FRIEND_NAME,
            entityColumn = DBContract.TableMessage.COLUMN_FKCHAT
    )
    public List<Message> message;

}