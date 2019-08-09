package com.example.liteteleprompter.db;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;

import com.test.orabi.teleprompter.repository.roomDb.AppDatabase;

import org.junit.After;
import org.junit.Before;


public class DBTest {

    protected AppDatabase db;


    @Before
    public void initDb(){
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),AppDatabase.class)
                .build();
    }


    @After
    public void closeDb(){
        db.close();
    }




}
