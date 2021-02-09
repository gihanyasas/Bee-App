package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText EmailText, regpassword, reenterpassword;
    private Button RegButton;
    private TextView alreadyloginbtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener firebaseAuthStatelistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EmailText = findViewById(R.id.Emailtxt);
        regpassword = findViewById(R.id.regpassword);
        reenterpassword = findViewById(R.id.reenterpassword);
        progressDialog = new ProgressDialog(Register.this);
        RegButton = findViewById(R.id.RegButton);
        alreadyloginbtn = findViewById(R.id.alreadyloginbtn);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait While checking");

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EmailText.getText().toString().trim();
                String password = regpassword.getText().toString().trim();
                String confirmpassword = reenterpassword.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    EmailText.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    regpassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    regpassword.setError("Password Must be >= 6 Characters");
                    return;
                }
                if (confirmpassword.isEmpty() || !confirmpassword.equals(password)) {
                    reenterpassword.setError("Password not matched");
                }


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.show();
                        if (task.isSuccessful()) {
                            FirebaseUser user =mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "User Created. \n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SetupActivity.class));

                        } else {
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alreadyloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginOrRegister.class));

            }
        });
    }

}