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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andriod.notes.MainActivity;
import com.andriod.notes.R;

public class FolderFragment extends Fragment {

    private static final int ADD_FOLDER_MENU_ITEM_ID = View.generateViewId();
    private MainActivity mainActivity;
    private LinearLayout listContainer;
    private Controller controller;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        controller = (Controller) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_folder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listContainer = view.findViewById(R.id.list_container);
        for (int i = 0; i < mainActivity.getFolderSize(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(mainActivity.getFolder(i));
            final int index = i;
            textView.setOnClickListener(v -> controller.folderPicked(index));
            listContainer.addView(textView);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem item = menu.add(Menu.NONE, ADD_FOLDER_MENU_ITEM_ID, 1, "Add folder");
        item.setIcon(R.drawable.ic_create_new_folder_24);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == ADD_FOLDER_MENU_ITEM_ID) {
            controller.folderAddNew();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
