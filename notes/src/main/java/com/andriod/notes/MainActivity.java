package com.andriod.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.andriod.notes.entity.Note;
import com.andriod.notes.fragment.Controller;
import com.andriod.notes.fragment.FolderFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Controller {

    private List<String> folders = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();
    private String currentFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new FolderFragment())
                .commit();
    }

    public int getFolderSize() {
        return folders.size();
    }

    public String getFolder(int index){
        return folders.get(index);
    }

    public List<Note> getNotes() {
        return notes;
    }

    @Override
    public void folderPicked(int index) {
        currentFolder = getFolder(index);
        Toast.makeText(this, String.format("Folder picked: %s", currentFolder), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void folderAddNew() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}