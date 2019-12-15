package com.example.smartfloodsystem;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.firebase.firestore.FirebaseFirestore;


public class ThreadsPageFragment extends Fragment {
    private SearchView mSearchThread;
    private ImageButton mAddThread;
    private RecyclerView mRecyclerThreadsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_threads_page, container, false);

        mSearchThread = (SearchView) v.findViewById(R.id.threads_search);
        mSearchThread.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        mAddThread = (ImageButton) v.findViewById(R.id.threads_add);
        mAddThread.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NewThreadsFragment newThreadsFragment = new NewThreadsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, newThreadsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        mRecyclerThreadsView = (RecyclerView) v.findViewById(R.id.threads_recycler_view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        

        return v;
    }

}
