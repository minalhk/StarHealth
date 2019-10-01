package com.example.starhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private EditText usernametv;
    private EditText passwordtv;
    private TextView info;
    private TextView forgotPasswordtv;
    private Button Login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public void onClickRegister(View view){
        finish();
        startActivity(new Intent(MainActivity.this,Registration.class));
    }
    public void onClickLogin(View view){
        validate(usernametv.getText().toString().trim(),passwordtv.getText().toString().trim());
    }

    public void onClickForgotPasswordTV(View view){
        finish();
        startActivity(new Intent(MainActivity.this,forgotPassword.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       firebaseAuth = FirebaseAuth.getInstance();
       progressDialog = new ProgressDialog(this);

       FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
           finish();
            startActivity(new Intent(MainActivity.this, DoctorActivity.class));
        }

        setUIviews();

    }

    private void setUIviews(){

        info = (TextView) findViewById(R.id.RegistertextView);
        info.setPaintFlags(info.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        forgotPasswordtv = (TextView) findViewById(R.id.forgotPasswordTextview);
        forgotPasswordtv.setPaintFlags(forgotPasswordtv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        usernametv = (EditText) findViewById(R.id.usernameEditText);


        passwordtv = (EditText) findViewById(R.id.passwordEditText);


        Login = (Button)findViewById(R.id.LoginButton);


    }

    public void validate(String username, String password){




        if((username.equals("Admin")) && (password.equals("12345"))){
            finish();
            startActivity(new Intent(MainActivity.this,AdminPage.class));
        }


        else if((username.equals("minalkokade0317@gmail.com")) && (password.equals("1234567"))){
            finish();
            startActivity(new Intent(MainActivity.this, DoctorActivity.class));
        }

        else if(((username == null)||(username.length() == 0)) || ((password == null)||(password.length()==0))){
            Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
        }

        else{
            //Toast.makeText(this,"Wrong credentials try again",Toast.LENGTH_SHORT).show();

            progressDialog.setMessage("Please wait..");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        //Toast.makeText(MainActivity.this,"Login successful!",Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(MainActivity.this, PatientActivity.class));
                        checkEmailVerification();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Incorrect Email_id or password",Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailVerification = firebaseUser.isEmailVerified();

        if(emailVerification){
            finish();
            startActivity(new Intent(MainActivity.this,PatientActivity.class));
        }
        else{
            Toast.makeText(this,"Please Verify your Email id, Verification link is sent already!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }


}
