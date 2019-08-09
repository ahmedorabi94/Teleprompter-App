package com.test.orabi.teleprompter.repository.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tele_table")
public class Tele {


    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;


    public Tele() {
    }

    public Tele(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Tele(int id ,String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
