package com.andriod.notes;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
    public static final String SEARCH_RESULTS = "SEARCH_RESULTS\n##";

    private static class ApplicationData {
        private final List<String> folders = new ArrayList<>();
        private final List<Note> notes = new ArrayList<>();
        private String currentFolder = "";
        private List<Note> currentFolderNotes = new ArrayList<>();
        private FragmentType currentFragment;
    }

    private ApplicationData data;

    private final Gson gson = new Gson();

    private BottomNavigationView bottomNavigationView;

    public enum FragmentType {
        FoldersList(true), FolderCreate(false), NotesList(true), NoteCreate(false), Settings(true);
        private final boolean returnable;

        FragmentType(boolean returnable) {
            this.returnable = returnable;
        }

        public boolean isReturnable() {
            return returnable;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, String.format("onCreate() called with: savedInstanceState = %s", savedInstanceState));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            Log.d(TAG, String.format("setOnNavigationItemSelectedListener() called: %s", item.getTitle()));

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

        if (savedInstanceState == null) {
            data = new ApplicationData();
        } else {
            restoreData(savedInstanceState);
        }

        if (data.currentFragment == null) data.currentFragment = FragmentType.FoldersList;

        updateScreen(R.id.container, data.currentFragment);
    }

    private void showSettings() {
        updateScreen(R.id.container, FragmentType.Settings);
    }

    @Override
    public void setBottomMenu(FragmentType currentFragmentType) {

        int checkedId = -1;
        int visibility = View.VISIBLE;

        switch (currentFragmentType) {
            case FoldersList:
                checkedId = R.id.menu_bottom_item_list;
                break;

            case FolderCreate:
                visibility = View.GONE;
                break;

            case NoteCreate:
                visibility = View.GONE;
                break;

            case NotesList:
                if (data.currentFolder.equals(FAVORITES))
                    checkedId = R.id.menu_bottom_item_favorites;
                break;

            case Settings:
                checkedId = R.id.menu_bottom_item_settings;
                break;

            default:
                return;
        }

        Log.d(TAG, String.format("setBottomMenu() called with: currentFragmentType = %s checkedId = %d", currentFragmentType.name(), checkedId));

        Menu menu = bottomNavigationView.getMenu();
        menu.setGroupCheckable(0, true, false);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == checkedId);
        }
        menu.setGroupCheckable(0, true, true);


        bottomNavigationView.setVisibility(visibility);

        data.currentFragment = currentFragmentType;
    }

    private void showFoldersFragment() {
        Log.d(TAG, "showFoldersFragment() called");
        updateScreen(R.id.container, FragmentType.FoldersList);
    }

    private void goBackToFoldersFragment() {
        Log.d(TAG, "goBackToFoldersFragment() called");
        updateScreen(R.id.container, FragmentType.FoldersList, true);
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
        updateScreen(R.id.container, FragmentType.NotesList, pickedFolder);
    }

    private void updateScreen(@IdRes int containerViewId, FragmentType fragmentType) {
        updateScreen(containerViewId, fragmentType, null, false);
    }

    private void updateScreen(@IdRes int containerViewId, FragmentType fragmentType, Object more) {
        updateScreen(containerViewId, fragmentType, more, false);
    }

    private void updateScreen(@IdRes int containerViewId, FragmentType fragmentType, boolean goBack) {
        updateScreen(containerViewId, fragmentType, null, goBack);
    }

    private void updateScreen(@IdRes int containerViewId, FragmentType fragmentType, Object more, boolean goBack) {

        Fragment fragment;

        switch (fragmentType) {
            case FoldersList:
                fragment = new FolderFragment();
                break;

            case FolderCreate:
                fragment = new FolderAddNewFragment();
                break;

            case NoteCreate:
                fragment = NoteCreateFragment.newInstance((Note) more);
                break;

            case NotesList:
                fragment = ListNotesFragment.newInstance((String) more);
                break;

            case Settings:
                fragment = new SettingsFragment();
                break;

            default:
                return;
        }

        if (goBack) {
            getSupportFragmentManager().popBackStack();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(containerViewId, fragment);
            if (data.currentFragment != fragmentType && data.currentFragment.isReturnable()) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

    @Override
    public void folderAddNew() {
        Log.d(TAG, "folderAddNew()");

        updateScreen(R.id.container, FragmentType.FolderCreate);
    }

    @Override
    public void folderAddNewResult(String newFolderName) {
        if (newFolderName != null) {
            data.folders.add(newFolderName);
            Log.d(TAG, String.format("folderAddNewResult() new folder added: %s", newFolderName));
        }
        Log.d(TAG, "folderAddNewResult() back to FolderFragment...");
        goBackToFoldersFragment();
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

        updateScreen(R.id.container, FragmentType.NoteCreate, newNote);
    }

    @Override
    public void deleteAll() {
        Log.d(TAG, "deleteAll() called");
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
        if (!folder.equals(data.currentFolder)) {
            data.currentFolder = folder;
            data.currentFolderNotes = new ArrayList<>();

            switch (folder) {
                case FAVORITES:
                    for (Note note : data.notes) {
                        if (note.isFavorite()) {
                            data.currentFolderNotes.add(note);
                        }
                    }
                    break;
                case SEARCH_RESULTS:
                    break;
                default:
                    for (Note note : data.notes) {
                        if (note.getFolder().equals(data.currentFolder)) {
                            data.currentFolderNotes.add(note);
                        }
                    }
                    break;
            }
        }

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

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed() called");
        super.onBackPressed();
    }
}