package com.example.smartfloodsystem;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class NewThreadsFragment extends Fragment {
    private Threads mThread;
    private EditText mThreadTitle;
    private EditText mThreadContent;
    private ImageView mThreadImage;
    private Drawable mWarningIcon;
    private Button mPostThread;
    private Uri mImageUri;
    private StorageReference storageRef;
    private FirebaseFirestore mCurrentUser;

    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int GALLERY_REQUEST_CODE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_threads, container, false);

        mWarningIcon = (Drawable) getResources().getDrawable(R.drawable.ic_alert_red_icon);
        mWarningIcon.setBounds(0,0, mWarningIcon.getIntrinsicWidth(), mWarningIcon.getIntrinsicHeight());

         mCurrentUser = FirebaseFirestore.getInstance();
         storageRef = FirebaseStorage.getInstance().getReference();

        mThreadTitle = (EditText) v.findViewById(R.id.new_thread_title);
        mThreadTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mThreadTitle.getText().toString().trim().isEmpty()) {
                    mThreadTitle.setError("Please enter a title for the thread", mWarningIcon);
                }
            }
        });

        mThreadContent = (EditText) v.findViewById(R.id.new_thread_content);
        mThreadContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mThreadContent.getText().toString().trim().isEmpty()) {
                    mThreadContent.setError("Please enter content for the thread", mWarningIcon);
                }
            }
        });
        mThreadImage = (ImageView) v.findViewById(R.id.new_thread_imageView);
        mThreadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageOptions(getContext());

            }
        });

        mPostThread = (Button) v.findViewById(R.id.button_postThread);
        mPostThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThread = new Threads();
                Date date = new Date();
                mThread.setmThreadTitle(mThreadTitle.getText().toString());
                mThread.setmThreadDate(date);
                mThread.setmThreadContent(mThreadContent.getText().toString());
                mThread.setmPostUserName(SignInFragment.sCurrentUser.getUserFirstName() + " " + SignInFragment.sCurrentUser.getUserLastName());
                if(mImageUri != null) {
                    uploadImage();
                }
                postThread(mThread);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ThreadsPageFragment()).addToBackStack(null).commit();


            }
        });


        return v;
    }

    private void postThread(final Threads threads) {
        ThreadsPageFragment threadsPageFragment = new ThreadsPageFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, threadsPageFragment)
                .addToBackStack(null)
                .commit();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("threads").add(threads).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                db.collection("threads").document(documentReference.getId()).update("mThreadId", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Thread Add", "Success");
                    }
                });            }
        });

    }

    private void selectImageOptions (Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (options[i].equals("Take Photo")) {
                    selectFromCamera();
                } else if (options[i].equals("Choose from Gallery")) {
                    selectFromGallery();
                } else if (options[i].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setType("image/*");
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void selectFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void uploadImage() {
        StorageReference filepath = storageRef.child("Thread_Images").child(mImageUri.getLastPathSegment());
        filepath.putFile(mImageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                mThread.setmThreadImageUri(downloadUrl.toString());
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    mImageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                        mThreadImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case CAMERA_REQUEST_CODE:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    mThreadImage.setImageBitmap(bitmap);
                    break;
            }
        }


    }


}
