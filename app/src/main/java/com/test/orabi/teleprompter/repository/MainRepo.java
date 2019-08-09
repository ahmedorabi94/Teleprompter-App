package com.test.orabi.teleprompter.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.test.orabi.teleprompter.AppExecutors;
import com.test.orabi.teleprompter.R;
import com.test.orabi.teleprompter.repository.data.Tele;
import com.test.orabi.teleprompter.repository.roomDb.TeleDao;

import java.util.List;

import javax.inject.Inject;

public class MainRepo {

    private Application mApplication;
    private AppExecutors appExecutors;
    private TeleDao teleDao;
    private LiveData<List<Tele>> allTeleLiveData;


    @Inject
    public MainRepo(Application application, TeleDao teleDao, AppExecutors appExecutors) {
        this.mApplication = application;
        this.appExecutors = appExecutors;
        this.teleDao = teleDao;
        getAllTeles();

    }

    private void getAllTeles() {
        allTeleLiveData = teleDao.getAllTeles();
    }

    public void deleteAllTeles() {
        appExecutors.diskIO().execute(teleDao::deleteAllTeles);

    }

    public void deleteTele(int id) {
        appExecutors.diskIO().execute(() -> teleDao.deleteTele(id));
    }


    public void insertDummyTele() {
        Tele tele = new Tele(mApplication.getString(R.string.lores_ipsum), mApplication.getString(R.string.some_text));

        appExecutors.diskIO().execute(() -> teleDao.insert(tele));

    }


    public LiveData<List<Tele>> getAsLiveData() {
        return allTeleLiveData;
    }

}
