package com.example.securerepo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.Objects;

@Entity (tableName = "notes_table")
public class Note {

    @PrimaryKey (autoGenerate = true)
    private int id;
    @ColumnInfo (typeAffinity = ColumnInfo.BLOB)
    private byte[] title;
    @ColumnInfo (typeAffinity = ColumnInfo.BLOB)
    private byte[] body;

    public Note() {
    }

    public Note(int id, byte[] title, byte[] body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public byte[] getTitle() {
        return title;
    }

    public void setTitle(byte[] title) {
        this.title = title;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void eraseNoteFields (){
        for (int i = 0; i < title.length; i ++) {
            title[i] = '0';
        }
        for (int j = 0; j < body.length; j ++) {
            body[j] = '0';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id &&
                Arrays.equals(title, note.title) &&
                Arrays.equals(body, note.body);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(title);
        result = 31 * result + Arrays.hashCode(body);
        return result;
    }
}
