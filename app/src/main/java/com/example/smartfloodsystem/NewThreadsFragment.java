package com.example.smartfloodsystem;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class NewThreadsFragment extends Fragment {
    private Threads mThread;
    private EditText mThreadTitle;
    private Spinner mThreadLocation;
    private EditText mThreadContent;
    private ImageView mThreadImage;
    private Drawable mWarningIcon;
    private Button mPostThread;
    private Uri mImageUri;
    private StorageReference storageRef;
    private FirebaseFirestore mCurrentUser;
    private int imageSelected;
    private String imageDownloadUri;
    private ProgressBar mProgressBar;
    private TextView mProgressText;
    private String currentPhotoPath;
    private Uri photoURI;
    private File photoFile;

    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int GALLERY_REQUEST_CODE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_threads, container, false);

        imageSelected = 0;
        mProgressBar = v.findViewById(R.id.progress_bar);
        mProgressText = v.findViewById(R.id.uploading_image);

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

        mThreadLocation = (Spinner) v.findViewById(R.id.new_thread_location);
        String[] locations = new String[]{"PJS7", "PJS9", "PJS11", "Jalan Universiti"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, locations);
        mThreadLocation.setAdapter(adapter);

        mThreadLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mLocation = parent.getItemAtPosition(position).toString();
                //Log.d("myApp", mLocation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView)mThreadLocation.getSelectedView()).setError(getResources().getString(R.string.register_error_empty_location), mWarningIcon);
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

                if(mThreadTitle.getText() != null && mThreadContent.getText() != null) {
                    mThread = new Threads();
                    Date date = new Date();
                    mThread.setmThreadTitle(mThreadTitle.getText().toString());
                    mThread.setmThreadDate(date);
                    mThread.setmThreadContent(mThreadContent.getText().toString());
                    mThread.setmPostUserName(SignInFragment.sCurrentUser.getUserFirstName() + " " + SignInFragment.sCurrentUser.getUserLastName());
                    mThread.setmThreadLocation(mThreadLocation.getSelectedItem().toString());
                if (imageSelected == 1) {
                    mThread.setmThreadImageUri(imageDownloadUri);
                }
                postThread(mThread);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ThreadsPageFragment()).addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), "There are Empty Fields", Toast.LENGTH_SHORT).show();
                }


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
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            imageSelected = 1;
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                        "com.example.smartfloodsystem.fileprovider",
                        photoFile);
                Log.d("PhotoFile", "PhotoFile" + photoURI);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private void selectFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imageSelected = 1;
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void uploadImage() {
        final StorageReference filepath = storageRef.child("Thread_Images").child(mImageUri.getLastPathSegment());
        filepath.putFile(mImageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Error", "Exception", e);
                Toast.makeText(getActivity(), "Upload Image Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageDownloadUri = uri.toString();
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mProgressText.setVisibility(View.INVISIBLE);

                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                        mImageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                            mThreadImage.setImageBitmap(bitmap);
                            uploadImage();
                            mProgressBar.setVisibility(View.VISIBLE);
                            mProgressText.setVisibility(View.VISIBLE);
                            //Glide.with(getActivity().getApplicationContext()).load(mImageUri).into(mThreadImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case CAMERA_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        Log.d("RIPP", "CAMERA_REQUEST_CODE");
                        Bitmap bitmap = BitmapFactory.decodeFile(photoURI.toString());
                        mImageUri = photoURI;
                        mThreadImage.setImageURI(photoURI);
                        //Glide.with(getActivity().getApplicationContext()).load(photoURI.toString()).into(mThreadImage);
                        uploadImage();
                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressText.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            Log.d("RIPPED", "CAMERA_REQUEST_CODE");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
