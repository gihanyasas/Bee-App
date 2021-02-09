package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginOrRegister extends AppCompatActivity {
    Button registerbutton,loginbutton;
     FirebaseAuth mAuth;
     EditText loginemail,password;
     TextView forgotpassword;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);


         loginemail = findViewById(R.id.loginemail);
         password = findViewById(R.id.password);
         mAuth = FirebaseAuth.getInstance();
         loginbutton = findViewById(R.id.loginbutton);
         registerbutton = findViewById(R.id.registerbutton);
        forgotpassword = findViewById(R.id.forgotpassword);

       loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                String email = loginemail.getText().toString().trim();
                String pswrd = password.getText().toString().trim();
                registerbutton = findViewById(R.id.registerbutton);


                if (TextUtils.isEmpty(email)) {
                    loginemail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(pswrd)) {
                    password.setError("Password is Required.");
                    return;
                }

                if (pswrd.length() < 6) {
                    password.setError("Password Must be >= 6 Characters");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, pswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginOrRegister.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(LoginOrRegister.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }


       });
       registerbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent (getApplicationContext(),Register.class);
               startActivity(i);

           }
       });
       forgotpassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final EditText resetMail = new EditText(view.getContext());
               AlertDialog.Builder passwordresetdialog = new AlertDialog.Builder(view.getContext());
               passwordresetdialog.setTitle("Reset Password");
               passwordresetdialog.setMessage("Enter your log in e mail address");
               passwordresetdialog.setView(resetMail);

               passwordresetdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       String mail = resetMail.getText().toString();
                       mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Toast.makeText(LoginOrRegister.this,"Reset Link Sent to your email",Toast.LENGTH_SHORT).show();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(LoginOrRegister.this,"E mail is not correct"+e.getMessage(),Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
               });
               passwordresetdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                        // close the dialog
                   }
               });
               passwordresetdialog.create().show();
           }
       });

    }
}