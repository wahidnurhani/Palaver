package de.unidue.palaver.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.uicontroller.arrayadapter.FriendArrayAdapter;

public class FriendManager {

    private List<Friend> friendList;
    private FriendArrayAdapter friendArrayAdapter;

    FriendManager() {
        this.friendList = new ArrayList<>();
    }

    public FriendArrayAdapter getFriendArrayAdapter() {
        return friendArrayAdapter;
    }

    public boolean removeFriend(String username){
        Friend friend = new Friend(username);
        return friendList.remove(friend);
    }

    public void sort(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            friendList.sort((o1, o2) -> o1.getUsername().compareTo(o2.getUsername()));
        }
    }

    public List<Friend> search(String string){
        List<Friend> result = new ArrayList<>();
        friendArrayAdapter.clear();
        if(string.equals("")){
            friendArrayAdapter.addAll(friendList);
            result = friendList;
        } else {
            friendArrayAdapter.clear();
            for (Friend friend: friendList ) {
                if(friend.getUsername().contains(string)){
                    result.add(friend);
                }
            }
            friendArrayAdapter.addAll(result);
        }
        return result;
    }

    public void updateFriends() {
        FetchFriendFromDB fetchFriendFromDB = new FetchFriendFromDB();
        fetchFriendFromDB.execute();
    }

    public void initArrayAdapter(Context context, int layout) {
        this.friendArrayAdapter = new FriendArrayAdapter(context, layout);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchFriendFromDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            PalaverDB palaverDB = Palaver.getInstance().getPalaverDB();
            friendList.clear();
            friendList.addAll(palaverDB.getAllFriends());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            friendArrayAdapter.clear();
            friendArrayAdapter.addAll(friendList);
        }
    }

//    public class DeleteContactTask extends AsyncTask<Friend,Void,List<FriendManagerActivity>>{
//
//        @Override
//        protected List<Friend> doInBackground(Friend... contact) {
//            try {
//                communicator.deleteContact(contact[0]);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            SQLiteDBAdapter sqliteDBAdapter = SQLiteDBAdapter.getPalaverDBOpenHelperInstanz(getActivity());
//            List<Contact> contactList = sqliteDBAdapter.getAllContact();
//            return contactList;
//        }
//
//        @Override
//        protected void onPostExecute(List<Contact> contacts) {
//            contactAdapter.clear();
//            Toast toast = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                toast = Toast.makeText(getContext(),"contact deleted",Toast.LENGTH_SHORT);
//            }
//            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
//            toast.show();
//            for (Contact tmp :contacts){
//                contactAdapter.add(tmp);
//            }
//        }
//    }
}
