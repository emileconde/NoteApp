package com.example.android.conde.com.note.threading;

import android.os.AsyncTask;

import com.example.android.conde.com.note.Models.Note;
import com.example.android.conde.com.note.persistence.NoteDao;


public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public InsertAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNotes(notes);
        return null;
    }

}