package com.test.orabi.teleprompter.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.test.orabi.teleprompter.AppExecutors;
import com.test.orabi.teleprompter.R;
import com.test.orabi.teleprompter.repository.data.Tele;
import com.test.orabi.teleprompter.repository.roomDb.AppDatabase;
import com.test.orabi.teleprompter.repository.roomDb.TeleDao;

import java.util.List;

public class MainRepo {

    private Application application;
    private AppExecutors appExecutors;
    private TeleDao teleDao;

    private MediatorLiveData<List<Tele>> mediatorLiveData;

    private static MainRepo repo;


    public static MainRepo getInstance(Application application) {
        if (repo == null) {
            repo = new MainRepo(application);
        }

        return repo;
    }



    public MainRepo(Application application) {
        this.application = application;
        this.appExecutors = new AppExecutors();
        this.teleDao = AppDatabase.getInstance(application).teleDao();
        this.mediatorLiveData = new MediatorLiveData<>();
        getAllTeles();

    }

    private void getAllTeles() {
        mediatorLiveData.addSource(teleDao.getAllTeles(),teles -> mediatorLiveData.setValue(teles));

    }

    public void deleteAllTeles() {

        appExecutors.diskIO().execute(teleDao::deleteAllTeles);

        mediatorLiveData.addSource(teleDao.getAllTeles(), teles -> mediatorLiveData.setValue(teles));

    }


    public void insertDummyTele() {

        Tele tele = new Tele(application.getString(R.string.lores_ipsum), application.getString(R.string.some_text));

        appExecutors.diskIO().execute(() -> teleDao.insert(tele));

        mediatorLiveData.addSource(teleDao.getAllTeles(), teles -> mediatorLiveData.setValue(teles));


    }


    public LiveData<List<Tele>> getAsLiveData() {
        return mediatorLiveData;
    }

}
