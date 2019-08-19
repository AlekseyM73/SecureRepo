package com.example.securerepo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.securerepo.model.PasswordChecker;

import io.reactivex.Single;

@Dao
public interface PasswordCheckerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPasswordChecker (PasswordChecker checker);

    @Query("SELECT * from password_checker_table")
    Single<PasswordChecker> getPasswordChecker ();
}
