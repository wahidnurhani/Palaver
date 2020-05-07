package de.unidue.palaver.system.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ListLiveData<T> extends MutableLiveData<List<T>> {

    public void addAll(List<T> items) {
        if (getValue() != null && items != null) {
            getValue().addAll(items);
            setValue(getValue());
        }
    }

    public void add( T item){
        if(getValue() != null && item != null){
            getValue().add(item);
            setValue(getValue());
        }
    }

    public void override(List<T> items){
        if (getValue() != null && items != null) {
            clear();
            getValue().addAll(items);
            setValue(getValue());
        }
    }

    private void clear() {
        if (getValue() != null) {
            getValue().clear();
            setValue(getValue());
        }
    }

    @Override public void setValue(List<T> value) {
        super.setValue(value);
    }

    @Nullable
    @Override public List<T> getValue() {
        return super.getValue();
    }
}

