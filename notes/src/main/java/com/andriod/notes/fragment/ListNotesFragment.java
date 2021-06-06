package com.andriod.notes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andriod.notes.MainActivity;
import com.andriod.notes.R;
import com.andriod.notes.entity.Note;

import java.util.List;

public class ListNotesFragment extends Fragment {
    private static final int ADD_NOTE_MENU_ITEM_ID = View.generateViewId();
    private static final String FOLDER_NAME_KEY = "folder name";

    private String folderName;
    private MainActivity mainActivity;
    private LinearLayout listContainer;
    private Controller controller;

    public static ListNotesFragment newInstance(String folderName) {
        ListNotesFragment fragment = new ListNotesFragment();
        Bundle fragmentData = new Bundle();
        fragmentData.putString(FOLDER_NAME_KEY, folderName);
        fragment.setArguments(fragmentData);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle fragmentData = getArguments();
        if (fragmentData != null) this.folderName = fragmentData.getString(FOLDER_NAME_KEY);

        setHasOptionsMenu(!folderName.equals(MainActivity.FAVORITES));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        controller = (Controller) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textViewFolderName = view.findViewById(R.id.text_view_folder_name);
        textViewFolderName.setText(folderName.equals(MainActivity.FAVORITES) ? getString(R.string.favorites_folder_title) : folderName);

        List<Note> notes = mainActivity.getNotesInFolder(folderName);

        listContainer = view.findViewById(R.id.list_container);
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getHeader().isEmpty()) continue;

            TextView textView = new TextView(getContext());
            textView.setText(notes.get(i).getHeader());
            final int index = i;
            textView.setOnClickListener(v -> controller.notePicked(index));
            listContainer.addView(textView);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem item = menu.add(Menu.NONE, ADD_NOTE_MENU_ITEM_ID, 1, "Add Note");
        item.setIcon(R.drawable.ic_create_new_note_24);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == ADD_NOTE_MENU_ITEM_ID) {
            controller.noteAddNew(folderName);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
