package com.test.orabi.teleprompter.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.test.orabi.teleprompter.repository.AddTextRepo;
import com.test.orabi.teleprompter.repository.data.Tele;

public class AddTextViewModel extends AndroidViewModel {

    private final LiveData<Tele> teleLiveData;
    private AddTextRepo repo;


    public AddTextViewModel(@NonNull Application application) {
        super(application);
        repo = AddTextRepo.getInstance(application);
        teleLiveData = repo.getAsLiveData();

    }


    public void getTele(int id) {
        repo.getTele(id);
    }


    public void insertNewTele(String title, String body) {

        repo.insertNewTele(title, body);

    }


    public int updateTele(int id, String title, String body) {

        return repo.updateTele(id, title, body);
    }


    public void deleteTele(int id) {
        repo.deleteTele(id);

    }


    public LiveData<Tele> getTeleLiveData() {
        return teleLiveData;
    }

}
