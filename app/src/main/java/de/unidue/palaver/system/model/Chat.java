package de.unidue.palaver.system.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import de.unidue.palaver.system.roomdatabase.DBContract;

public class Chat implements Comparable<Chat>{

    @Embedded public Friend friend;
    @Relation(
            parentColumn =DBContract.TableFriend.COLUMN_FRIEND_NAME,
            entityColumn = DBContract.TableMessage.COLUMN_FKFRIEND
    )
    public List<Message> messages;

    public Message getLastMessage() {
        if(messages.size()>0){
            List<Message> messages1 = messages;
            Collections.sort(messages1);
            return messages1.get(messages.size()-1);
        }
        return null;
    }

    public Friend getFriend() {
        return friend;
    }

    @Override
    public int compareTo(Chat o) {
        if(this.getLastMessage()!=null && o.getLastMessage()!=null){
            try {
                return o.getLastMessage().getDateDate().compareTo(this.getLastMessage().getDateDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}