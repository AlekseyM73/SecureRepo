package com.example.securerepo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "notes_table")
public class Note {

    @PrimaryKey (autoGenerate = true)
    private int id;
    private char[] title;
    private char[] body;

    public Note() {
    }

    public Note(int id, char[] title, char[] body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public char[] getTitle() {
        return title;
    }

    public void setTitle(char[] title) {
        this.title = title;
    }

    public char[] getBody() {
        return body;
    }

    public void setBody(char[] body) {
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
}
