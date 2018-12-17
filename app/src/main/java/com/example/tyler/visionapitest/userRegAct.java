package com.example.tyler.visionapitest;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class userRegAct extends AppCompatActivity {
    @BindView(R.id.regemails)
    EditText usremail;
    @BindView(R.id.edPW1)
    EditText usrpw01;
    @BindView(R.id.edPW2)
    EditText usrpw02;

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_createAcc)
    public void regUser() {
        String email = usremail.getText().toString();
        String password = usrpw01.getText().toString();


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(userRegAct.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(userRegAct.this, "Account Created", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(userRegAct.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
