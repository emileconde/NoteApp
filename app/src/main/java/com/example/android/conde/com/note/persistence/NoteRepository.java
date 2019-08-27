package com.example.android.conde.com.note.persistence;


import android.content.Context;

import com.example.android.conde.com.note.Models.Note;
import com.example.android.conde.com.note.threading.DeleteAsyncTask;
import com.example.android.conde.com.note.threading.InsertAsyncTask;
import com.example.android.conde.com.note.threading.UpdateAsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;


public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note){
        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNoteTask(Note note){
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask() {
        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNoteTask(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }
}













