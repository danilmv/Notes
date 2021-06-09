package com.andriod.notes.fragment;

import com.andriod.notes.MainActivity;

public interface Controller {
    void folderPicked(int index);
    void folderAddNew();
    void folderAddNewResult(String newFolderName);
    void notePicked(int index);
    void noteAddNew(String folder);
    void deleteAll();
    void setBottomMenu(MainActivity.FragmentType currentFragmentType);
}
