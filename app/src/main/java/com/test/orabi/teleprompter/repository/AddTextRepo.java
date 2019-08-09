package com.test.orabi.teleprompter.repository;

import androidx.lifecycle.LiveData;

import com.test.orabi.teleprompter.AppExecutors;
import com.test.orabi.teleprompter.repository.data.Tele;
import com.test.orabi.teleprompter.repository.roomDb.TeleDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AddTextRepo {

    private AppExecutors appExecutors;
    private TeleDao teleDao;
    private LiveData<Tele> teleLiveData;


    @Inject
    public AddTextRepo(TeleDao teleDao, AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        this.teleDao = teleDao;
    }


    public void getTele(int id) {
        teleLiveData = teleDao.getTele(id);
    }


    public void insertNewTele(String title, String body) {
        Tele tele = new Tele(title, body);
        appExecutors.diskIO().execute(() -> teleDao.insert(tele));
    }

    public void deleteTele(int id) {
        appExecutors.diskIO().execute(() -> teleDao.deleteTele(id));
    }

    public int updateTele(int id, String title, String body) {
        final int[] rowId = new int[1];
        appExecutors.diskIO().execute(() -> rowId[0] = teleDao.updateTele(id, title, body));
        return rowId[0];
    }

    public LiveData<Tele> getAsLiveData() {
        return teleLiveData;
    }


}
