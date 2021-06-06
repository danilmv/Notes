package com.andriod.notes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andriod.notes.R;

public class FolderAddNewFragment extends Fragment {
    private Controller controller;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        controller = (Controller) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_folder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextFolderName = view.findViewById(R.id.edit_text_folder_name);

        view.findViewById(R.id.button_cancel)
                .setOnClickListener(v -> controller.folderAddNewResult(null));

        view.findViewById(R.id.button_save)
                .setOnClickListener(v -> {
                    String folderName = editTextFolderName.getText().toString();
                    if (folderName.isEmpty()) {
                        Toast.makeText(getContext(), "Enter folder name, please.", Toast.LENGTH_SHORT).show();
                    } else {
                        controller.folderAddNewResult(folderName);
                    }
                });
    }
}
