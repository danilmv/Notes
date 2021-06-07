package com.andriod.notes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
    private static final String TAG = "@ListNotesFragment@";

    private String folderName;
    private MainActivity mainActivity;
    private Controller controller;

    public static ListNotesFragment newInstance(String folderName) {
        ListNotesFragment fragment = new ListNotesFragment();
        fragment.setArguments(folderName);

        return fragment;
    }

    public void setArguments(String folderName) {
        Bundle fragmentData = new Bundle();
        fragmentData.clear();
        fragmentData.putString(FOLDER_NAME_KEY, folderName);
        this.setArguments(fragmentData);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach() called with: context = [" + context + "]");
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        controller = (Controller) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called");
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated() called with: savedInstanceState = [" + savedInstanceState + "]");

        super.onViewCreated(view, savedInstanceState);

        Bundle fragmentData = getArguments();
        if (fragmentData != null) this.folderName = fragmentData.getString(FOLDER_NAME_KEY);

        setHasOptionsMenu(!folderName.equals(MainActivity.FAVORITES));

        TextView textViewFolderName = view.findViewById(R.id.text_view_folder_name);
        textViewFolderName.setText(folderName.equals(MainActivity.FAVORITES) ? getString(R.string.favorites_folder_title) : folderName);

        List<Note> notes = mainActivity.getNotesInFolder(folderName);

        LinearLayout listContainer = view.findViewById(R.id.list_container);
        for (int i = 0; i < notes.size(); i++) {
            String header = notes.get(i).getHeader();
            if (header == null || header.isEmpty()) continue;

            TextView textView = new TextView(getContext());
            textView.setText(header);
            final int index = i;
            textView.setOnClickListener(v -> controller.notePicked(index));
            listContainer.addView(textView);
        }
        controller.setBottomMenu(MainActivity.FragmentType.NotesList);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
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
