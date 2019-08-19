package com.example.securerepo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "password_checker_table")
public class PasswordChecker {

    @PrimaryKey
    private int id = 1;
    private byte [] bytesToCheck;

    public PasswordChecker(byte[] bytesToCheck) {
        this.bytesToCheck = bytesToCheck;
    }

    public int getId() {
        return id;
    }

    public byte[] getBytesToCheck() {
        return bytesToCheck;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBytesToCheck(byte[] bytesToCheck) {
        this.bytesToCheck = bytesToCheck;
    }
}
