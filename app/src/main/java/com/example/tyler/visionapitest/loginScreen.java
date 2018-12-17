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

public class loginScreen extends AppCompatActivity {

@BindView(R.id.etUsername)EditText username1;
@BindView(R.id.etpasswordinput)EditText password1;
@BindView(R.id.btn_login)Button login1;
@BindView(R.id.btn_forgotPW)Button forgotpw1;
@BindView(R.id.btn_createAcc)Button createaccount1;
@BindView(R.id.btn_guest)Button guestlogin1;
private FirebaseAuth mAuth;
private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();


    }


    @OnClick(R.id.btn_guest)
    public void LoginAsGuest(View view){
    Toast resultToast = Toast.makeText(getApplicationContext(), "You Logged in as Guest", Toast.LENGTH_LONG);
    resultToast.show();
    Intent intent = new Intent(loginScreen.this, MainActivity.class);
    startActivity(intent);
    }

    @OnClick(R.id.btn_createAcc)
    public void CreateAccount(View v){
        Intent regIntent = new Intent(loginScreen.this, userRegAct.class);
        startActivity(regIntent);
    }

    @OnClick(R.id.btn_forgotPW)
    public void onForgotPassPress(View v){
        Intent forgotIntent = new Intent(loginScreen.this, ForgotPassActivity.class);
        startActivity(forgotIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if ( currentUser!= null ) {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent intent = new Intent(loginScreen.this, NavigationActivity.class);
        intent.putExtra("usrnme",currentUser);
        startActivity(intent);

    }


    @OnClick(R.id.btn_login)
    public void usrlogin(View view){
        String email = username1.getText().toString();
        String password = password1.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }




}


