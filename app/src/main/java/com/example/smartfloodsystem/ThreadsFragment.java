package com.example.smartfloodsystem;


import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ThreadsFragment extends Fragment {
    private TextView mThreadTitle;
    private ImageView mThreadImage;
    private TextView mThreadContent;
    private Button mAddComments;
    private RecyclerView mThreadComments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_threads, container, false);

        mThreadTitle = (TextView) v.findViewById(R.id.thread_title);
        mThreadImage = (ImageView) v.findViewById(R.id.thread_imageView);
        mThreadContent = (TextView) v.findViewById(R.id.thread_content);
        mAddComments = (Button) v.findViewById(R.id.button_addComments);
        mThreadComments = (RecyclerView) v.findViewById(R.id.thread_comments_recycler_view);

        return v;
    }

}
