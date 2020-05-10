package de.unidue.palaver.system.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.unidue.palaver.system.model.Chat;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;

public class ChatsViewModel extends AndroidViewModel {

    private ListLiveData<Chat> chatListLiveData;
    private PalaverDao palaverDao;

    public ChatsViewModel(Application application) {
        super(application);

        PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(application);
        palaverDao = palaverRoomDatabase.palaverDao();
        this.chatListLiveData = new ListLiveData<>();
        this.chatListLiveData.setValue(new ArrayList<>());
        this.fetchChats();
    }

    public ListLiveData<Chat> getChatListLiveData() {
        return chatListLiveData;
    }

    public void removeChat(Chat chat){
        new RemoveChatFromDB().execute(chat);
    }

    private void fetchChats() {
        FectchChatListFromDB fectchChatListFromDB = new FectchChatListFromDB();
        fectchChatListFromDB.execute();
    }

    public List<Chat> search(String newText) {
        List<Chat> searched = new ArrayList<>();
        for (Chat chat: Objects.requireNonNull(chatListLiveData.getValue())){
            if(chat.getFk_friend().contains(newText)){
                searched.add(chat);
            }
        }
        if(newText.equals("")){
            searched =chatListLiveData.getValue();
        }
        return searched;
    }

    @SuppressLint("StaticFieldLeak")
    private class FectchChatListFromDB extends AsyncTask<Void, Void, List<Chat>> {

        @Override
        protected List<Chat> doInBackground(Void... voids) {
            List<Chat> result= palaverDao.loadAllChat();
            List<Chat> returnValue = new ArrayList<>();
            for (Chat chat : result){
                if(chat.getData()!=null){
                    returnValue.add(chat);
                }
            }
            return returnValue;
        }

        @Override
        protected void onPostExecute(List<Chat> chats) {
            Collections.sort(chats);
            chatListLiveData.override(chats);
        }
    }

    private class RemoveChatFromDB extends AsyncTask<Chat, Void, Void>{

        @Override
        protected Void doInBackground(Chat... chats) {
            return null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
