package com.test.orabi.teleprompter.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.test.orabi.teleprompter.AppExecutors;
import com.test.orabi.teleprompter.repository.data.Tele;
import com.test.orabi.teleprompter.repository.roomDb.AppDatabase;
import com.test.orabi.teleprompter.repository.roomDb.TeleDao;

public class AddTextRepo {

    private AppExecutors appExecutors;
    private static AddTextRepo repo;
    private TeleDao teleDao;
    private LiveData<Tele> teleLiveData;


    public static AddTextRepo getInstance(Application application) {
        if (repo == null) {
            repo = new AddTextRepo(application);
        }
        return repo;
    }

    private AddTextRepo(Application application) {
        appExecutors = new AppExecutors();
        teleDao = AppDatabase.getInstance(application).teleDao();
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
