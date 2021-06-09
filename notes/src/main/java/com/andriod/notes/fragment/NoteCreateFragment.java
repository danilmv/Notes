package com.andriod.notes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andriod.notes.MainActivity;
import com.andriod.notes.R;
import com.andriod.notes.entity.Note;
import com.google.android.material.textfield.TextInputEditText;

public class NoteCreateFragment extends Fragment {
    private static final String NOTE_KEY = "note";
    private static final String TAG = "@NoteCreateFragment@";
    private Note note;
    private TextInputEditText editTextHeader;
    private Controller controller;

    public static NoteCreateFragment newInstance(Note note) {
        NoteCreateFragment noteCreateFragment = new NoteCreateFragment();
        noteCreateFragment.setArguments(note);

        return noteCreateFragment;
    }

    public void setArguments(Note note) {
        Bundle data = new Bundle();
        data.clear();
        data.putParcelable(NOTE_KEY, note);
        this.setArguments(data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        controller = (Controller) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getArguments();
        if (data != null) this.note = (Note) data.getParcelable(NOTE_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextHeader = view.findViewById(R.id.edit_text_header);

        controller.setBottomMenu(MainActivity.FragmentType.NoteCreate);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState() called with: outState = [" + outState + "]");
        super.onSaveInstanceState(outState);
        note.setHeader(editTextHeader.getText().toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        note.setHeader(editTextHeader.getText().toString());
    }
}
