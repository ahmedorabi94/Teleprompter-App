package com.test.orabi.teleprompter.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.test.orabi.teleprompter.R;
import com.test.orabi.teleprompter.repository.MainRepo;
import com.test.orabi.teleprompter.repository.data.Tele;
import com.test.orabi.teleprompter.view.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private Application application;

    private LiveData<List<Tele>> allTelesLiveData;

    private MainRepo repo;


    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repo = MainRepo.getInstance(application);
        allTelesLiveData = repo.getAsLiveData();


    }


    public void deleteAllTeles() {
        repo.deleteAllTeles();
    }

    public void insertDummyTele() {
        repo.insertDummyTele();
    }


    public String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = application.getContentResolver().openInputStream(uri);
        assert inputStream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);

        }
        inputStream.close();
        reader.close();
        return stringBuilder.toString();
    }


    public void showPopup(View view, MainActivity mainActivity) {
        PopupMenu popup = new PopupMenu(application, view);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(mainActivity);
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();

    }

    public LiveData<List<Tele>> getAllTelesLiveData() {
        return allTelesLiveData;
    }


}
