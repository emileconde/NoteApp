package com.example.android.conde.com.note;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.android.conde.com.note.Models.Note;
import com.example.android.conde.com.note.adapter.NoteRecyclerAdapter;
import com.example.android.conde.com.note.persistence.NoteRepository;
import com.example.android.conde.com.note.util.VerticalSpacingItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class NoteListActivity extends AppCompatActivity implements
        NoteRecyclerAdapter.OnNoteListener,
        FloatingActionButton.OnClickListener {

    private static final String TAG = "NotesListActivity";

    // ui components
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFabAddNote;
    private CoordinatorLayout mCoordinatorLayout;
    private TextView mAddNotesTextView;

    // vars
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    private NoteRepository mNoteRepository;
    private boolean mIsEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        mRecyclerView = findViewById(R.id.recyclerView);
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mAddNotesTextView = findViewById(R.id.tv_add_note);

        mFabAddNote = findViewById(R.id.fab);
        mFabAddNote.setOnClickListener(this);

        initRecyclerView();
        mNoteRepository = new NoteRepository(this);
        retrieveNotes();

        setSupportActionBar((Toolbar) findViewById(R.id.notes_toolbar));
        setTitle(R.string.toolbar_title);


    }



    private void retrieveNotes() {
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {

                if (mNotes.size() > 0) {
                    mNotes.clear();
                }
                if (notes != null) {
                    mNotes.addAll(notes);
                    if(mNotes.size() == 0){
                        mIsEmpty = true;
                        centerFab();
                    }else {
                        mIsEmpty = false;
                        centerFab();
                    }
                }
                mNoteRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(mNotes, this);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }


    @Override
    public void onNoteClick(int position) {
//        Intent intent = new Intent(this, NoteActivity.class);
//        intent.putExtra("selected_note", mNotes.get(position));
//        startActivity(intent);
        slideToNoteActivity(mNotes.get(position));
    }

    @Override
    public void onClick(View view) {
//        Intent intent = new Intent(this, NoteActivity.class);
//        startActivity(intent);
        slideToNoteActivity(null);
    }

    private void deleteNote(Note note) {
        mNotes.remove(note);
        mNoteRecyclerAdapter.notifyDataSetChanged();

        mNoteRepository.deleteNoteTask(note);
        Snackbar.make(mFabAddNote, R.string.note_deleted, Snackbar.LENGTH_SHORT).show();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };

    //centers the button if there is no note
    private void centerFab(){
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mFabAddNote.getLayoutParams();
        if(mIsEmpty) {
            layoutParams.gravity = Gravity.CENTER;
            mAddNotesTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else {
            layoutParams.gravity = Gravity.BOTTOM|Gravity.RIGHT;
            mAddNotesTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mFabAddNote.setLayoutParams(layoutParams);
    }


    private void slideToNoteActivity(Note note){
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        Intent i = new Intent(this, NoteActivity.class);
        if(note != null)
            i.putExtra("selected_note", note);
        startActivity(i, options.toBundle());

    }

}