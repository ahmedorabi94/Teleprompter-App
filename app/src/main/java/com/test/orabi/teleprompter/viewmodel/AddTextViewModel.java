package com.test.orabi.teleprompter.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.test.orabi.teleprompter.repository.AddTextRepo;
import com.test.orabi.teleprompter.repository.data.Tele;

import javax.inject.Inject;

public class AddTextViewModel extends AndroidViewModel {

    private final LiveData<Tele> teleLiveData;
    private AddTextRepo repo;

    private MutableLiveData<Integer> mId = new MutableLiveData<>();


    @Inject
    public AddTextViewModel(@NonNull Application application , AddTextRepo repo) {
        super(application);
        this.repo = repo;
        teleLiveData = Transformations.switchMap(mId,id ->
                repo.getAsLiveData()
                );

    }


    public void getTele(int id) {
        repo.getTele(id);
        mId.setValue(id);
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
