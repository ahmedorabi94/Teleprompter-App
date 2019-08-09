package com.test.orabi.teleprompter.repository.roomDb;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.test.orabi.teleprompter.repository.data.Tele;

@Database(entities = {Tele.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TeleDao teleDao();


}
