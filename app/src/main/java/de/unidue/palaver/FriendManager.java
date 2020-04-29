package de.unidue.palaver;

import android.os.Build;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.unidue.palaver.model.Friend;

public class FriendManager {

    private List<Friend> friendList;

    public FriendManager(Palaver palaver) {
        this.friendList = new ArrayList<>();
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public Friend getFriend(String username){
        for (Friend friend : friendList) {
            if(friend.getUsername().equals(username)){
                return friend;
            }
        }
        return null;
    }

    public void addFriend(Friend friend){
        friendList.add(friend);
    }

    public boolean removeFriend(String username){
        Friend friend = new Friend(username);
        return friendList.remove(friend);
    }

    public void sort(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            friendList.sort(new Comparator<Friend>() {
                @Override
                public int compare(Friend o1, Friend o2) {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            });
        }
    }

    public List<Friend> search(String string){
        List<Friend> result = new ArrayList<>();
        for (Friend friend: friendList ) {
            if(friend.getUsername().contains(string)){
                result.add(friend);
            }
        }
        return result;
    }

    public void openFriendListActivity(){

    }

    public void openAddFriendDialog(){

    }

    public void refreshView(){

    }
}
