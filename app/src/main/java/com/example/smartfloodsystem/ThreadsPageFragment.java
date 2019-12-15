package com.example.smartfloodsystem;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ThreadsPageFragment extends Fragment {
    private SearchView mSearchThread;
    private ImageButton mAddThread;
    private RecyclerView mRecyclerThreadsView;
    private FirebaseAuth mAuth;
    private ListenerRegistration fireStoreListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_threads_page, container, false);


        mRecyclerThreadsView = (RecyclerView) v.findViewById(R.id.threads_recycler_view);
        mRecyclerThreadsView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null) {
                    new AlertDialog.Builder(getContext()).setTitle("User Not Logged In").setMessage("You need to login to post a new thread.").setPositiveButton(R.string.new_threads_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                } else {
                    NewThreadsFragment newThreadsFragment = new NewThreadsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, newThreadsFragment)
                            .addToBackStack(null)
                            .commit();
                }

            }
        });

        mRecyclerThreadsView = (RecyclerView) v.findViewById(R.id.threads_recycler_view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fireStoreListener = db.collection("threads").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<Threads> mThreads = new ArrayList<Threads>();
                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                    Threads mThread = document.toObject(Threads.class);
                    mThreads.add(mThread);
                }
                ThreadsAdapter threadsAdapter = new ThreadsAdapter(mThreads);
                mRecyclerThreadsView.setAdapter(threadsAdapter);
            }
        });


        return v;
    }

    @Override
    public void onDestroy() {
        fireStoreListener.remove();
        super.onDestroy();
    }

    private class ThreadsAdapter extends RecyclerView.Adapter<ThreadsHolder> {

        private List<Threads> mThreads;

        public ThreadsAdapter(List<Threads> threads) {
            mThreads = threads;
        }

        @NonNull
        @Override
        public ThreadsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ThreadsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ThreadsHolder holder, int position) {
            Threads threads = mThreads.get(position);
            holder.bind(threads);
        }

        @Override
        public int getItemCount() {
            return mThreads.size();
        }
    }

    private class ThreadsHolder extends RecyclerView.ViewHolder {

        private Threads mThreads;

        private TextView mThreadsPageThreadsTitle;
        private TextView mThreadsPageThreadsDate;
        private TextView mThreadsPageThreadsByWho;

        public ThreadsHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.list_item_threads, parent, false));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, ThreadsFragment.newThreadsFragment(mThreads)).addToBackStack(null).commit();
                }
            });

            mThreadsPageThreadsTitle = (TextView) itemView.findViewById(R.id.threads_page_threads_title);
            mThreadsPageThreadsDate = (TextView) itemView.findViewById(R.id.threads_page_threads_date);
            mThreadsPageThreadsByWho = (TextView) itemView.findViewById(R.id.threads_page_threads_by_who);
        }

        public void bind(Threads threads) {
            mThreads = threads;
            mThreadsPageThreadsTitle.setText(mThreads.getmThreadTitle());
            mThreadsPageThreadsDate.setText(mThreads.getmThreadDate().toString());
            String textForUser = getResources().getString(R.string.threads_page_by_user, mThreads.getmPostUserName());
            mThreadsPageThreadsByWho.setText(textForUser);
        }
    }

}
