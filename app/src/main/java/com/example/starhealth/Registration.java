package com.example.starhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
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



public class Registration extends AppCompatActivity {

    private EditText username,password,email,confirmPassword;
    private TextView backtologin;
    private Intent intent;

    private FirebaseAuth firebaseAuth;

    //String emailString,usernameString;


    public void onClickRegister(View view){

        String user_username = username.getText().toString().trim();
        String user_password = password.getText().toString().trim();
        String user_email = email.getText().toString().trim();
        String confirm_password = confirmPassword.getText().toString().trim();

       if(validate(user_username,user_password,user_email,confirm_password)){

           firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {

                   if (task.isSuccessful()) {

                      // Toast.makeText(Registration.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                       //finish();
                        //startActivity(intent);
                        sendEmailVerificationLink();
                   }
                   else{
                       Toast.makeText(Registration.this, "Something went wrong Registration was Unsuccessful", Toast.LENGTH_SHORT).show();

                   }
               }
           });

        }
    }


    public void onClickBacktologin(View view){

        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        setUIcomponents();
    }

    private void setUIcomponents(){
        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordEditText);

        backtologin = (TextView) findViewById(R.id.backtologinTextView);
        backtologin.setPaintFlags(backtologin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        intent = new Intent(Registration.this, MainActivity.class);
    }

    private boolean validate(String username, String password, String email, String confirm_passwrd){
        boolean result=false;
        if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT).show();

            return result;
        }
        else if(password.length()<7){
            Toast.makeText(this,"password size should be more than 6 characters",Toast.LENGTH_SHORT).show();
            return result;
        }
        else if(!password.equals(confirm_passwrd)){
            Toast.makeText(this, "Please check password field and enter right password",Toast.LENGTH_SHORT).show();
            return result;
        }
        return  true;

    }

    private void sendEmailVerificationLink(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        userData();
                        Toast.makeText(Registration.this,"Email verification link is sent to you, Please verify your email",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Registration.this,MainActivity.class));
                    }
                    else {
                        Toast.makeText(Registration.this,"Something went wrong,Please try again!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private  void  userData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());

        RegistrationUserInfo registrationUserInfo = new RegistrationUserInfo(username.getText().toString(),email.getText().toString().trim());
        myRef.setValue(registrationUserInfo);

    }
}
