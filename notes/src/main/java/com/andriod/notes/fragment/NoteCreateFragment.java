package com.andriod.notes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andriod.notes.R;
import com.andriod.notes.entity.Note;
import com.google.android.material.textfield.TextInputEditText;

public class NoteCreateFragment extends Fragment {
    private static final String NOTE_KEY = "note";
    private Note note;
    private TextInputEditText editTextHeader;

    public static NoteCreateFragment newInstance(Note note){
        NoteCreateFragment noteCreateFragment = new NoteCreateFragment();
        Bundle data = new Bundle();
        data.putParcelable(NOTE_KEY, note);
        noteCreateFragment.setArguments(data);

        return noteCreateFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getArguments();
        if (data!=null) this.note = (Note)data.getParcelable(NOTE_KEY);
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
    }

    @Override
    public void onStop() {
        super.onStop();

        note.setHeader(editTextHeader.getText().toString());
    }
}
