package com.test.orabi.teleprompter.repository.roomDb;


import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.test.orabi.teleprompter.repository.data.Tele;

@Database(entities = {Tele.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TeleDao teleDao();

    private static AppDatabase appDatabase;


    // Singleton Pattern
    public static synchronized AppDatabase getInstance(Application application) {

        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(application, AppDatabase.class, "tele.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return appDatabase;

    }
}
