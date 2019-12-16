package com.example.smartfloodsystem;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ThreadsFragment extends Fragment {
    private TextView mThreadTitle;
    private TextView mThreadLocation;
    private ImageView mThreadImage;
    private TextView mThreadContent;
    private Button mAddComments;
    private RecyclerView mThreadComments;
    private EditText mThreadAddCommentEditText;

    public static final String newThreadsTitle = "com.example.smartFloodSystem.threadsFragment.newThreadsTitle";

    public static final ThreadsFragment newThreadsFragment(Threads threads) {
        ThreadsFragment threadsFragment = new ThreadsFragment();
        Bundle args = new Bundle();
        args.putSerializable(newThreadsTitle, threads);
        threadsFragment.setArguments(args);
        return threadsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_threads, container, false);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        mThreadTitle = (TextView) v.findViewById(R.id.thread_title);
        mThreadLocation = (TextView) v.findViewById(R.id.thread_location);
        mThreadImage = (ImageView) v.findViewById(R.id.thread_imageView);
        mThreadContent = (TextView) v.findViewById(R.id.thread_content);
        mThreadLocation = (TextView) v.findViewById(R.id.thread_location);
        mThreadAddCommentEditText = (EditText) v.findViewById(R.id.threads_add_comment_edit_text);
        mAddComments = (Button) v.findViewById(R.id.threads_add_comments_button);
        mThreadComments = (RecyclerView) v.findViewById(R.id.thread_comments_recycler_view);
        mThreadComments.setLayoutManager(new LinearLayoutManager(getContext()));

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Threads threads = (Threads) getArguments().getSerializable(newThreadsTitle);
        mThreadTitle.setText(threads.getmThreadTitle());
        mThreadContent.setText(threads.getmThreadContent());
        if(threads.getmThreadImageUri() != null) {
            Glide.with(getActivity().getApplicationContext())
                    .load(threads.getmThreadImageUri())
                    .into(mThreadImage);
        }

        CommentsAdapter commentsAdapter = new CommentsAdapter(threads.getmThreadComment());
        mThreadComments.setAdapter(commentsAdapter);

        mAddComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    new AlertDialog.Builder(getContext()).setTitle("User Not Logged In").setMessage("You need to login to leave a comment.").setPositiveButton(R.string.new_threads_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                } else if (mThreadAddCommentEditText.getText().toString().trim().length() == 0){
                    Toast.makeText(getContext(), "Please enter a comment.", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> comments = new HashMap<>();
                    comments.put(threads.getmPostUserName(), mThreadAddCommentEditText.getText().toString());
                    db.collection("threads").document(threads.getmThreadId()).update("mThreadComment", FieldValue.arrayUnion(comments)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Add Comment", "Success");
                            mThreadAddCommentEditText.setText("");
                        }
                    });
                }
            }
        });


        return v;
    }

    private class CommentsAdapter extends RecyclerView.Adapter<CommentsHolder> {

        private List<HashMap<String, String>> mComments;

        public CommentsAdapter(List<HashMap<String, String>> comments) {
            mComments = comments;
        }

        @NonNull
        @Override
        public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CommentsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentsHolder holder, int position) {
            HashMap<String, String> comment = mComments.get(position);
            holder.bind(comment);
        }

        @Override
        public int getItemCount() {
            if (mComments != null) {
                return mComments.size();
            }
            return 0;
        }
    }

    private class CommentsHolder extends RecyclerView.ViewHolder {

        private TextView mThreadsCommentContent;
        private TextView mThreadsCommentor;

        public CommentsHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.list_item_threads_comments, parent, false));

            mThreadsCommentContent = (TextView) itemView.findViewById(R.id.threads_comment_content);
            mThreadsCommentor = (TextView) itemView.findViewById(R.id.threads_comment_commentor);
        }

        public void bind(HashMap<String, String> comment) {
            Set<String> keys = comment.keySet();
            Iterator keysIterator = keys.iterator();
            String commentor = (String) keysIterator.next();

            String content = comment.get(commentor);
            Log.d("bugger", content);

            mThreadsCommentContent.setText(content);
            mThreadsCommentor.setText(commentor);
        }
    }

}
