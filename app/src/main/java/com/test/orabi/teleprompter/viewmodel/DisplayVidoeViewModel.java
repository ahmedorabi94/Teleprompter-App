package com.test.orabi.teleprompter.viewmodel;

import android.app.Application;
import android.content.ContentValues;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;

import javax.inject.Inject;

public class DisplayVidoeViewModel extends AndroidViewModel {

    private Application mApplication;

    private MutableLiveData<Boolean> isDeleted ;
    private MutableLiveData<Boolean> isSaved = new MutableLiveData<>();
    private MutableLiveData<Boolean> isVideoFound = new MutableLiveData<>();


    @Inject
    public DisplayVidoeViewModel(@NonNull Application application) {
        super(application);

        mApplication = application;
        isDeleted= new MutableLiveData<>();
        isDeleted.setValue(false);
    }


    public void deleteFile(String videoPath) {

        if (videoPath == null) {
            isVideoFound.setValue(false);
            return;
        }
        File file = new File(videoPath);
        isDeleted.setValue(file.delete());


    }


    public void saveFile(String videoPath) {

        if (isDeleted.getValue()) {
            isVideoFound.setValue(false);
            return;
        }
        File file = new File(videoPath);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.DATA, file.getAbsolutePath());
        mApplication.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        isSaved.setValue(true);
    }


    public LiveData<Boolean> getIsDeleted() {
        return isDeleted;
    }

    public LiveData<Boolean> getIsSaved() {
        return isSaved;
    }


    public LiveData<Boolean> getIsVideoFound() {
        return isVideoFound;
    }
}
