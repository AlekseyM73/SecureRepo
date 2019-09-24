package com.alekseymakarov.locknote.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.alekseymakarov.locknote.model.PasswordChecker;

import io.reactivex.Single;

@Dao
public interface PasswordCheckerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPasswordChecker (PasswordChecker checker);

    @Update
    void updatePasswordChecker(PasswordChecker checker);

    @Query("SELECT * from password_checker_table where id = :id")
    Single<PasswordChecker> getPasswordChecker (int id);
}
