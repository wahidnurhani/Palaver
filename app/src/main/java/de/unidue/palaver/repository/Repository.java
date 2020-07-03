package de.unidue.palaver.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface Repository<T> {

    LiveData<List<T>> getLiveData();
    void add(T t);
    void delete(T t);
}
