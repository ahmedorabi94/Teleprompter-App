package com.test.orabi.teleprompter.di;

import android.app.Application;

import androidx.room.Room;

import com.test.orabi.teleprompter.repository.roomDb.AppDatabase;
import com.test.orabi.teleprompter.repository.roomDb.TeleDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    AppDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, AppDatabase.class, "tele.db").build();
    }

    @Singleton
    @Provides
    TeleDao provideTeleDao(AppDatabase db) {
        return db.teleDao();
    }


}
