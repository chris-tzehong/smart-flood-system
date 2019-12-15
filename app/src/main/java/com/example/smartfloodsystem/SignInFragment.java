package com.example.smartfloodsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class SignInFragment extends Fragment {
    private View v;
    private Button btnLogin;
    private EditText txtEmail, txtPassword;
    private String mEmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Drawable mWarningIcon;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    public static User sCurrentUser;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mWarningIcon = (Drawable) getResources().getDrawable(R.drawable.ic_alert_red_icon);
        mWarningIcon.setBounds(0,0, mWarningIcon.getIntrinsicWidth(), mWarningIcon.getIntrinsicHeight());
        txtEmail = v.findViewById(R.id.txt_email);
        txtPassword = v.findViewById(R.id.txt_password);

        mAuth = FirebaseAuth.getInstance();

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (txtEmail.getText().toString().trim().isEmpty())
                {
                    txtEmail.setError(getResources().getString(R.string.register_error_empty_email), mWarningIcon);
                }
                else if (!txtEmail.getText().toString().trim().matches(mEmailPattern))
                {
                    txtEmail.setError(getResources().getString(R.string.register_error_invalid_email), mWarningIcon);
                }
            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (txtPassword.getText().toString().isEmpty())
                {
                    txtPassword.setError(getResources().getString(R.string.register_error_empty_password), mWarningIcon);
                }
                else if (txtPassword.getText().toString().length() <= 8 || txtPassword.getText().toString().length() >= 20)
                {
                    txtPassword.setError(getResources().getString(R.string.register_error_invalid_password_length), mWarningIcon);
                }
            }
        });

        btnLogin = v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             if (txtEmail.getError() != null || txtPassword.getError() != null )
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Incorrect email address or password.");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
                else if (txtEmail.getText().toString() == "" || txtPassword.getText().toString() == "" )
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("The email adress and password must be entered.");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
                else
            {
                mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(getActivity(), "Successfully logged in.", Toast.LENGTH_SHORT).show();
                                    db.collection("users").whereEqualTo("userEmail", txtEmail.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                                                    sCurrentUser = documentSnapshot.toObject(User.class);
                                                }
                                            }
                                        }
                                    });
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new RegisterFragment()).addToBackStack(null).commit();


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            }

            }
        });



        return v;
    }
}
