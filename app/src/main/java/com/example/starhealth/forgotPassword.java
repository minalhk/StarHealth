package com.example.starhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    private EditText emailid;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;

    public void onClickPasswordReset(View view){
        String mailid = emailid.getText().toString().trim();

        if(mailid.isEmpty()){
            Toast.makeText(this,"Please enter your registered Email id",Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.sendPasswordResetEmail(mailid).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(forgotPassword.this,"Password reset link is sent successfully!",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(forgotPassword.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(forgotPassword.this,"This email id is not registered",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailid = (EditText) findViewById(R.id.forgotPasswordEmail);
        resetPassword = (Button) findViewById(R.id.passwordResetButton);

        firebaseAuth = FirebaseAuth.getInstance();
    }
}
