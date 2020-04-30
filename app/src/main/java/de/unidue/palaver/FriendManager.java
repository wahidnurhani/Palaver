package de.unidue.palaver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import de.unidue.palaver.database.PalaverDBManager;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.ui.AddFriendDialog;
import de.unidue.palaver.ui.ChatManagerActivity;
import de.unidue.palaver.ui.FriendManagerActivity;

public class FriendManager {

    private List<Friend> friendList;
    private FriendArrayAdapter friendArrayAdapter;

    public FriendManager() {
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

    public void search(String string){
        friendArrayAdapter.clear();
        for (Friend friend: friendList ) {
            if(friend.getUsername().contains(string)){
                friendArrayAdapter.add(friend);
            }
        }
        if(string.equals("")){
            friendArrayAdapter.clear();
            friendArrayAdapter.addAll(friendList);
        }
    }

    public void openFriendListActivity(Context context){
        FriendManagerActivity.startIntent(context);
    }

    public void openAddFriendDialog(Context appContext, Context context){
        new AddFriendDialog(appContext, context);
    }

    public void updateFriends() {
        FetchFriendFromDB fetchFriendFromDB = new FetchFriendFromDB();
        fetchFriendFromDB.execute();

    }

    public void initArrayAdapter(Context context, int textViewID, int layout) {
        this.friendArrayAdapter = new FriendArrayAdapter(context, textViewID, layout);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchFriendFromDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            PalaverDBManager palaverDBManager = Palaver.getInstance().getPalaverDBManager();
            friendList.clear();
            friendList.addAll(palaverDBManager.getAllFriends());

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
