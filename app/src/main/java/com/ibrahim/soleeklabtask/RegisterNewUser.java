package com.ibrahim.soleeklabtask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.nio.file.FileAlreadyExistsException;

public class RegisterNewUser extends AppCompatActivity {
    private static final String TAG = "RegisterNewUser";

    private FirebaseAuth mAuth;

    EditText emailEditText;
    EditText passEditText;
    EditText passEditTextConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        final com.google.firebase.auth.FirebaseAuthUserCollisionException exception;

        Button register = findViewById(R.id.registerButton);
        emailEditText = findViewById(R.id.emailEditText);
        passEditText = findViewById(R.id.PassEditText);
        passEditTextConfirm = findViewById(R.id.PassEditTextConfirm);

        register.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passEditText.getText().toString();
                String passwordConfirm = passEditTextConfirm.getText().toString();
                Log.d(TAG, "onClick: "+password+" "+passwordConfirm);

                //check if pass matches the confirmation
                if (!password.equals(passwordConfirm )){
                    showAlertDialoge("password miss match" , " ");
                    Log.d(TAG, "onClick: password miss match ");
                 return;
                }
//                Log.d(TAG, "onClick: pas"+password+" email---- "+email);
                //check for empty fields
                if (password.length()==0 || email.length()==0){
                    Log.d(TAG, "onClick: pas"+password+" email f sdfr rf "+email);
                showAlertDialoge("empty field" , "please enter a valid E-mail and Password");
                    return;
                }

                Log.d(TAG, "onClick: "+password);


                  mAuth.createUserWithEmailAndPassword(email, password)
                          .addOnCompleteListener(RegisterNewUser.this, new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {
                                  if (task.isSuccessful()) {

                                      // Sign in success
                                      Log.d(TAG, "createUserWithEmail:success");
                                      FirebaseUser user = mAuth.getCurrentUser();
                                      if (user != null){
                                          Intent intent = new Intent(getApplicationContext() , CountriesActivity.class);
                                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                          startActivity(intent);

                                      }



                                  } else {

                                      //  sign in fails
                                      Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                      try
                                      {
                                          throw task.getException();
                                      }
                                      // if user enters weak Password.
                                      catch (FirebaseAuthWeakPasswordException weakPassword)
                                      {
                                          Log.d(TAG, "onComplete: weak_password");
                                          showAlertDialoge("weak password" ,"please enter 6 digits at least" );

                                      }
                                      // if user enters malformed Email.
                                      catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                      {
                                          Log.d(TAG, "onComplete: malformed_email");
                                          showAlertDialoge("malformed Email" , "please enter a valid email format");
                                      }
                                      // if user enters existed Email.

                                      catch (FirebaseAuthUserCollisionException existEmail)
                                      {
                                          Log.d(TAG, "onComplete: exist_email");
                                          showAlertDialoge("Exist Email" , " this email already exist ");

                                          // TODO: reset password
                                      } catch (Exception e) {
                                          e.printStackTrace();
                                      }

                                      updateUI(null);

                                  }

                              }
                          }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {

                      }
                  });




            }
        });

        //login button
        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //return to login screen
                Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);            }
        });


    }

    private void updateUI(FirebaseUser currentUser)  {

    }

    private void showAlertDialoge(String title , String message){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
