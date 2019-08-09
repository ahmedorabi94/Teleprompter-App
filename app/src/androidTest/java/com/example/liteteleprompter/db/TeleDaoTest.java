package com.example.liteteleprompter.db;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.liteteleprompter.TestUtil;
import com.test.orabi.teleprompter.repository.data.Tele;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class TeleDaoTest extends DBTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void insertAndUpdate() {
        final Tele tele = TestUtil.createTele();
        db.teleDao().insert(tele);

        int id = db.teleDao().updateTele(tele.getId(), "Item 2", "Body 3");

        assertThat(id, is(tele.getId()));

    }


    @Test
    public void insertAndDelete() {
        final Tele tele = TestUtil.createTele();
        db.teleDao().insert(tele);

        db.teleDao().deleteTele(tele.getId());


    }


    public void getAllTeles() {
        db.teleDao().getAllTeles();
    }


}
