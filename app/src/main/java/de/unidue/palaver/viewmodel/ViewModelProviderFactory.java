package de.unidue.palaver.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.unidue.palaver.model.Friend;

public class ViewModelProviderFactory implements ViewModelProvider.Factory {
    Application application;
    Friend friend;

    public ViewModelProviderFactory(Application application) {
        this.application = application;
    }

    public ViewModelProviderFactory(Application application, Friend friend) {
        this.application = application;
        this.friend = friend;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ChatsViewModel.class)){
            return (T) new ChatsViewModel( application);
        } else if (modelClass.isAssignableFrom(MessageViewModel.class) ){
            return (T) new MessageViewModel(application, friend);
        } else if (modelClass.isAssignableFrom(FriendViewModel.class)){
            return (T) new FriendViewModel(application);
        } else if(modelClass.isAssignableFrom(LoginRegisterViewModel.class)){
            return (T) new LoginRegisterViewModel(application);
        } else if (modelClass.isAssignableFrom(SettingViewModel.class)){
            return (T) new SettingViewModel(application);
        } else {
            throw new IllegalArgumentException("ViewModel not found");
        }
    }
}
