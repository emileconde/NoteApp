package com.example.android.conde.com.note.persistence;


import android.content.Context;

import com.example.android.conde.com.note.Models.Note;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notes_db";

    private static NoteDatabase instance;

    static NoteDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract NoteDao getNoteDao();
}