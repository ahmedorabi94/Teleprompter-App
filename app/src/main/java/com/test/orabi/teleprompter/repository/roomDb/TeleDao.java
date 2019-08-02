package com.test.orabi.teleprompter.repository.roomDb;



import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.test.orabi.teleprompter.repository.data.Tele;

import java.util.List;

@Dao
public interface TeleDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tele tele);


    @Query("SELECT * From tele_table")
    LiveData<List<Tele>> getAllTeles();


    @Query("DELETE FROM tele_table")
    void deleteAllTeles();


    @Query("DELETE FROM tele_table WHERE id = :id")
    void deleteTele(int id);


    @Query("UPDATE tele_table SET title = :title , body = :body WHERE id = :tId")
    int updateTele(int tId, String title, String body);


    @Query("SELECT * FROM tele_table WHERE id = :tId")
    LiveData<Tele> getTele(int tId);


}
