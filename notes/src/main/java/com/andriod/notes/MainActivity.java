package com.andriod.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.andriod.notes.entity.Note;
import com.andriod.notes.fragment.Controller;
import com.andriod.notes.fragment.FolderAddNewFragment;
import com.andriod.notes.fragment.FolderFragment;
import com.andriod.notes.fragment.ListNotesFragment;
import com.andriod.notes.fragment.NoteCreateFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Controller {

    private static final String TAG = "@MainActivity@";
    private static final String SAVE_DATA = "data";

    private static class ApplicationData {
        private final List<String> folders = new ArrayList<>();
        private final List<Note> notes = new ArrayList<>();
        private String currentFolder;
        private List<Note> currentFolderNotes = new ArrayList<>();
    }

    private ApplicationData data;

    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            data = new ApplicationData();
        } else {
            restoreData(savedInstanceState);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new FolderFragment())
                .commit();
    }

    public int getFolderSize() {
        return data.folders.size();
    }

    public String getFolder(int index) {
        return data.folders.get(index);
    }

    public List<Note> getNotes() {
        return data.notes;
    }

    @Override
    public void folderPicked(int index) {
        Log.d(TAG, "folderPicked() called with: index = [" + index + "]");
        String pickedFolder = getFolder(index);
        if (!pickedFolder.equals(data.currentFolder)) {
            data.currentFolder = pickedFolder;
            data.currentFolderNotes = new ArrayList<>();
            for (Note note : data.notes) {
                if (note.getFolder().equals(data.currentFolder)) data.currentFolderNotes.add(note);
            }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, ListNotesFragment.newInstance(data.currentFolder))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void folderAddNew() {
        Log.d(TAG, "folderAddNew()");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new FolderAddNewFragment())
                .commit();
    }

    @Override
    public void folderAddNewResult(String newFolderName) {
        if (newFolderName != null) {
            data.folders.add(newFolderName);
            Log.d(TAG, String.format("folderAddNewResult() new folder added: %s", newFolderName));
        }
        Log.d(TAG, "folderAddNewResult() back to FolderFragment...");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new FolderFragment())
                .commit();
    }

    @Override
    public void notePicked(int index) {
        Log.d(TAG, String.format("notePicked(): index = %d header = %s", index, data.currentFolderNotes.get(index).getHeader()));
//        currentFolderNotes.get(index);
    }

    @Override
    public void noteAddNew(String folder) {
        Log.d(TAG, String.format("noteAddNew() folder = %s", folder));

        Note newNote = new Note(null, "123123", folder);
        data.notes.add(newNote);
        data.currentFolderNotes.add(newNote);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, NoteCreateFragment.newInstance(newNote))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public List<Note> getNotesInFolder(String folder) {
        return data.currentFolderNotes;
    }

    private String dataToString() {
        return gson.toJson(data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);

        outState.putString(SAVE_DATA, dataToString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);

        restoreData(savedInstanceState);
    }

    private void restoreData(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(SAVE_DATA))
            data = gson.fromJson(savedInstanceState.getString(SAVE_DATA), ApplicationData.class);
    }
}