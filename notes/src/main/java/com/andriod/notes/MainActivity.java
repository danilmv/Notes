package com.andriod.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.andriod.notes.entity.Note;
import com.andriod.notes.fragment.Controller;
import com.andriod.notes.fragment.FolderAddNewFragment;
import com.andriod.notes.fragment.FolderFragment;
import com.andriod.notes.fragment.ListNotesFragment;
import com.andriod.notes.fragment.NoteCreateFragment;
import com.andriod.notes.fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Controller {

    private static final String TAG = "@MainActivity@";
    private static final String SAVE_DATA = "data";
    public static final String FAVORITES = "FAVORITES\n##";

    private static class ApplicationData {
        private final List<String> folders = new ArrayList<>();
        private final List<Note> notes = new ArrayList<>();
        private String currentFolder;
        private List<Note> currentFolderNotes = new ArrayList<>();
    }

    private ApplicationData data;

    private final Gson gson = new Gson();

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            data = new ApplicationData();
        } else {
            restoreData(savedInstanceState);
        }

        bottomNavigationView = findViewById(R.id.bottom_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_bottom_item_list) {
                showFoldersFragment();
            } else if (itemId == R.id.menu_bottom_item_favorites) {
                showFolderContent(FAVORITES);
            } else if (itemId == R.id.menu_bottom_item_settings) {
                showSettings();
            } else {
                return false;
            }
            return true;
        });

        showFoldersFragment();
    }

    private void showSettings() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }

    private void setBottomMenu(int checkedId) {
        Menu menu = bottomNavigationView.getMenu();
        menu.setGroupCheckable(0, true, false);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == checkedId);
        }
        menu.setGroupCheckable(0, true, true);
    }

    private void showFoldersFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new FolderFragment())
                .commit();

        setBottomMenu(R.id.menu_bottom_item_list);
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
        showFolderContent(getFolder(index));
    }

    private void showFolderContent(String pickedFolder) {
        if (!pickedFolder.equals(data.currentFolder)) {
            data.currentFolder = pickedFolder;
            data.currentFolderNotes = new ArrayList<>();
            for (Note note : data.notes) {
                if (pickedFolder.equals(FAVORITES)) {
                    if (note.isFavorite()) data.currentFolderNotes.add(note);
                } else {
                    if (note.getFolder().equals(data.currentFolder))
                        data.currentFolderNotes.add(note);
                }
            }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, ListNotesFragment.newInstance(data.currentFolder))
                .addToBackStack(null)
                .commit();

        setBottomMenu(pickedFolder.equals(FAVORITES) ? R.id.menu_bottom_item_favorites : -1);
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
        showFoldersFragment();
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
        newNote.setFavorite(true);
        data.notes.add(newNote);
        data.currentFolderNotes.add(newNote);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, NoteCreateFragment.newInstance(newNote))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void deleteAll() {
        data.folders.clear();
        data.currentFolderNotes.clear();
        data.notes.clear();

        showFoldersFragment();
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