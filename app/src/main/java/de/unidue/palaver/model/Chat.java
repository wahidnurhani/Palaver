package de.unidue.palaver.model;

import androidx.room.DatabaseView;

@DatabaseView("SELECT * " +
        "FROM (select fk_friend, table_chat_data.data, date_time " +
        "from table_friend INNER JOIN table_chat_data " +
        "on fk_friend =friend_name order by date_time DESC) " +
        "GROUP By fk_friend ")
public class Chat implements Comparable<Chat>{

    public String fk_friend;
    public String data;
    public String date_time;

    public String getFk_friend() {
        return fk_friend;
    }

    public String getData() {
        return data;
    }

    public String getDate_time() {
        return date_time;
    }

    @Override
    public int compareTo(Chat o) {
        if(this.getData()!=null && o.getData()!=null){
            return o.getDate_time().compareTo(this.getDate_time());
        }
        return 0;
    }
}