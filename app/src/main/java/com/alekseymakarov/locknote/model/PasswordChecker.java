package com.alekseymakarov.locknote.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordChecker that = (PasswordChecker) o;
        return id == that.id &&
                Arrays.equals(bytesToCheck, that.bytesToCheck);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(bytesToCheck);
        return result;
    }
}
