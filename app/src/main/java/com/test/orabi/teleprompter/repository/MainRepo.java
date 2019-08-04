package com.test.orabi.teleprompter.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.test.orabi.teleprompter.AppExecutors;
import com.test.orabi.teleprompter.R;
import com.test.orabi.teleprompter.repository.data.Tele;
import com.test.orabi.teleprompter.repository.roomDb.AppDatabase;
import com.test.orabi.teleprompter.repository.roomDb.TeleDao;

import java.util.List;

public class MainRepo {

    private Application mApplication;
    private AppExecutors appExecutors;
    private TeleDao teleDao;
    private LiveData<List<Tele>> allTeleLiveData;
    private static MainRepo repo;


    public static MainRepo getInstance(Application application) {
        if (repo == null) {
            repo = new MainRepo(application);
        }

        return repo;
    }


    private MainRepo(Application application) {
        mApplication = application;
        appExecutors = new AppExecutors();
        teleDao = AppDatabase.getInstance(mApplication).teleDao();
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
