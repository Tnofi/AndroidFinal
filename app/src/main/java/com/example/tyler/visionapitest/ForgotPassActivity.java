package com.example.tyler.visionapitest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ForgotPassActivity extends AppCompatActivity {

    @BindView(R.id.etEmailforPass) EditText tEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSendEmail)
    public void SendEmail(View v){
        String email = tEmail.getText().toString();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast successToast = Toast.makeText(getApplicationContext(), "Email Sent. Please check your email for ", Toast.LENGTH_LONG);
                    successToast.show();
                } else {
                    Toast failedToast = Toast.makeText(getApplicationContext(), "There was  problem sending reset email. Please check if the email address was typed correctly and try again.", Toast.LENGTH_LONG);
                    failedToast.show();
                }
            }
        });
    }
}
