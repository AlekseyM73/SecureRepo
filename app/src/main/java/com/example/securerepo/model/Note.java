package com.example.securerepo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.Objects;

@Entity (tableName = "notes_table")
public class Note {

    @PrimaryKey (autoGenerate = true)
    private int id;
    private Character[] title;
    private Character[] body;

    public Note() {
    }

    public Note(int id, Character[] title, Character[] body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public Character[] getTitle() {
        return title;
    }

    public void setTitle(Character[] title) {
        this.title = title;
    }

    public Character[] getBody() {
        return body;
    }

    public void setBody(Character[] body) {
        this.body = body;
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
