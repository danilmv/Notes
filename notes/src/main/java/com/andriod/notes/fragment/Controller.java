package com.andriod.notes.fragment;

public interface Controller {
    void folderPicked(int index);
    void folderAddNew();
    void folderAddNewResult(String newFolderName);
    void notePicked(int index);
    void noteAddNew(String folder);
    void deleteAll();
}
